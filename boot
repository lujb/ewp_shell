#!/bin/bash

# init environment
home="$HOME/.ewp/ewp_shell.swp"
beamdir=$home/beams
if [ -d $home ]; then
	rm -rf $home/*
else
	mkdir -p $home
fi
mkdir -p $beamdir

function die() {
	echo "Error!";
	exit $(($1))
}

# self-extracting
cwd=$(cd $(dirname $0); pwd)
thisfilepath=$cwd/$(basename $0)
cd $home && sed -e '1,/^ESCRIPT$/d' "$thisfilepath" | tar xzf - && cd $cwd || die 170

# check debug flag
debug=:
if [ ! -z $1 ] && [ $1 == "-debug" ]; then
	debug="echo [DeBUG] "
	extra_flag="-debug"
fi

#
ps -ef | grep '\-[p]rogname erl' > $home/all_nodes
$home/kernel checkvm $extra_flag

#
if [ ! -f $home/ewp_nodes ]; then
	echo "No running ewp nodes."
	exit 180
fi

nodes=( $( cat $home/ewp_nodes ) )
if [ $((${#nodes[@]})) -eq 1 ]; then
	echo "Only one ewp node is running: ${nodes[$((0))]}"
	selected_node=${nodes[$((0))]}
elif [ $((${#nodes[@]})) -gt 1 ]; then
	echo "current running ewp nodes:"
	# show ewp nodes list
	echo "--------------------------"
	for i in $(seq 0 $((${#nodes[@]} - 1))); do
		echo "$(($i + 1))) ${nodes[$i]}"
	done

	# choose an ewp node
	echo -n "选择一个将导入beam的ewp节点(输入数字1~$((${#nodes[@]}))): "
	read i; echo

	if [ $i -eq $i 2>/dev/null ] && [ $i -ge 1 ] && [ $i -le $((${#nodes[@]})) ]; then
		selected_node=${nodes[$(($i - 1))]}
		echo "You choose $selected_node."
	else 
		echo "invalid input."
		exit 181
	fi
else
	echo "No running ewp nodes."
	exit 180
fi


# choose user ebin path
$home/kernel userebin --node $selected_node $extra_flag

selected_ebin='undefined'
if [ ! -f $home/user_ebin ]; then
	echo "警告：在节点$selected_node中未发现用户ebin路径，新增的beam将只导入到内存中！"
else
	ebin=( $( cat $home/user_ebin ) )
	if [ $((${#ebin[@]})) -eq 1 ]; then
		echo "新增的beam将会写入到此ebin路径: ${ebin[$((0))]}"
		selected_ebin=${ebin[$((0))]}
	elif [ $((${#ebin[@]})) -gt 1 ]; then
		echo
		echo "当前在节点$selected_node中的ebin路径有:"
		# show ebin list
		echo "--------------------------"
		for i in $(seq 0 $((${#ebin[@]} - 1))); do
			echo "$(($i + 1))) ${ebin[$i]}"
		done
		
		# choose an ebin path
		echo -n "选择一个ebin路径用以存放新增的beam(输入数字1~$((${#ebin[@]}))): "
		read i; echo

		if [ $i -eq $i 2>/dev/null ] && [ $i -ge 1 ] && [ $i -le $((${#ebin[@]})) ]; then
			selected_ebin=${ebin[$(($i - 1))]}
			echo "You choose $selected_ebin."
		else
			echo "invalid input"
			exit 181
		fi
	else
		echo "警告：未发现用户ebin路径，新增的beam将只导入到内存中！"
		#exit 180
	fi
fi


#
echo -n "请输入待导入的beam文件路径(支持单个beam文件或zip/tgz压缩文件):"
read beam
if [ ! -z $beam ] && [ -f $beam ]; then
	filename=$(basename "$beam")
	extension="${filename##*.}"
	$debug "Your input filetype is $extension"
	if [ $extension == "gz" ]; then
		filename="${filename%.*}"
		extension="${filename##*.}"
		if [ $extension == "tar" ]; then
			tar -zxf $beam -C $beamdir
			$home/kernel inject --beam $beamdir --node $selected_node --ebin $selected_ebin $extra_flag
		else
			echo "invalid input."
			exit 184
		fi
	elif [ $extension == "zip" ]; then
		unzip -q $beam -d $beamdir
		$home/kernel inject --beam $beamdir --node $selected_node --ebin $selected_ebin $extra_flag
	elif [ $extension == "beam" ]; then
		$home/kernel inject --beam $beam --node $selected_node --ebin $selected_ebin $extra_flag
	else
		echo "invalid input."
		exit 183
	fi
else
	echo "invalid input."
	exit 182
fi

# check result
kernel_exit=$?
if [ -f $home/new_beam ]; then
	echo -n "[INFO] 新增以下beam: "
	echo $(cat $home/new_beam)
else
	echo "[INFO] 没有新增的beam."
fi
if [ -f $home/replace_beam ]; then
	echo -n "[INFO] 替换以下beam: "
	echo $(cat $home/replace_beam)
else
	echo "[INFO] 没有替换的beam."
fi

# clear
$debug "clear template files."
rm -rf $home/*

# done
if [ $kernel_exit -eq 0 ]; then
	echo "Done!"
else
	echo "exit: $kernel_exit"
fi

exit 0

# kernel
ESCRIPT
