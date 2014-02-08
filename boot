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
echo "thisfilepath:$thisfilepath"
cd $home && sed -e '1,/^ESCRIPT$/d' "$thisfilepath" | tar xzf - && cd $cwd || die 170

# check debug flag
debug=:
if [ ! -z $1 ] && [ $1 == "-debug" ]; then
	debug="echo"
	extra_flag="-debug"
fi

#
ps -ef | grep '\-[p]rogname erl' > $home/all_nodes
$home/kernel checkvm $extra_flag

#
nodes=( $( cat $home/ewp_nodes ) )
if [ $((${#nodes[@]})) -gt 0 ]; then
	echo "current running ewp nodes:"
else
	echo "No running ewp nodes."
	exit 180
fi

#
echo "--------------------------"
for i in $(seq 0 $((${#nodes[@]} - 1))); do
	echo "$(($i + 1))) ${nodes[$i]}"
done

#
echo -n "choose an ewp node:"
read i
selected_node=${nodes[$(($i - 1))]}
echo
if [ -z $selected_node ]; then
	echo "invalid input."
	exit 181
else
	echo "You chose $selected_node."
fi

#
echo -n "specify beam path to inject:"
read beam
if [ -f $beam ]; then
	filename=$(basename "$beam")
	extension="${filename##*.}"
	$debug "Your input filetype is $extension"
	if [ $extension == "gz" ]; then
		filename="${filename%.*}"
		extension="${filename##*.}"
		if [ $extension == "tar" ]; then
			tar -zxf $beam -C $beamdir
			$home/kernel inject --beam $beamdir --node $selected_node $extra_flag
		else
			echo "invalid beam."
			exit 184
		fi
	elif [ $extension == "zip" ]; then
		unzip -q $beam -d $beamdir
		$home/kernel inject --beam $beamdir --node $selected_node $extra_flag
	elif [ $extension == "beam" ]; then
		$home/kernel inject --beam $beam --node $selected_node $extra_flag
	else
		echo "invalid beam."
		exit 183
	fi
else
	echo "invalid beam."
	exit 182
fi

# clear
$debug "clear template files."
rm -rf $home/*

# done
echo "Done!"
exit 0

# kernel
ESCRIPT
