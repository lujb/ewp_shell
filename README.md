ewp_shell
=========
管理ewp运行时的shell环境，提供开箱即用的体验(LOL)。

### 脚本生成 ###
在*nix环境下，make直接生成ewp_shell脚本:
```sh
make
```
此种方式默认生成的脚本是压缩的，若要生成非压缩格式的脚本，则：
```sh
make plain
```
所有的功能都包含在ewp_shell脚本中，直接使用这个脚本即可。

正常启动方式如下：
```sh
./ewp_shell
```
以调试方式启动如下：
```sh
./ewp_shell -debug
```

### Features ###
* 支持本地任意sname/name节点探测

### 动态更新ewp内存中的beam ###
1. 运行脚本；
2. 如果当前运行有多个ewp节点，脚本将列出所有节点，并要求用户选择一个节点；
3. 接着脚本会列出所有ebin路径，并要求用户选择一个ebin路径用来保存后续导入新增的beam(不是新增的则直接覆盖)；
4. 脚本接着会要求输入待加载的beam文件名或压缩包(支持zip和tar.gz格式)，如：path/to/file.beam或path/to/file.tar.gz；
5. 自动导入beam，回显统计数量；
6. 脚本结束；

示例：
```
kingbo@caught:~/src/gitrepo/ewp_shell$ ./ewp_shell 
current running ewp nodes:
--------------------------
1) ebank@127.0.0.1 	(name)long name mode.
2) ewp@caught 	(sname)short name mode.
选择一个将导入beam的ewp节点(输入数字1~2): 1

You choose ebank@127.0.0.1, type:name.

当前在节点ebank@127.0.0.1中的ebin路径有:
--------------------------
1) /usr/local/lib/yaws/examples/ebin
2) /home/kingbo/src/rytong/emp.proj/ebank/../ewp/ebin
3) .
4) /usr/local/lib/yaws/ebin
5) /home/kingbo/src/rytong/emp.proj/ewp/ebin
6) /home/kingbo/src/rytong/emp.proj/ebank/ebin
选择一个ebin路径用以存放新增的beam(输入数字1~6): 6

You choose /home/kingbo/src/rytong/emp.proj/ebank/ebin.

请输入待导入的beam文件路径(支持单个beam文件或zip/tgz压缩文件): ./tmp/tmp.zip 
[INFO] 没有新增的beam.
[INFO] 替换以下beam: abc.beam ebank_ass.beam
Done!
```
### Features in Future ###
* 外部ewp节点探测；
* erlang console接入；
* ewp集群节点状态管理；
* app的启动/关闭的支持；
* app部署；
* 远程操作；
* 脚本解释功能；

### Known Issues ###
在某些系统上运行压缩的ewp_shell脚本会报如下错误:
```
-bash-3.2$ ./ewp_shell
gzip: stdin is encrypted -- get newer version of gzip
tar: Child returned status 1
tar: Error exit delayed from previous errors
Error!
```
出现这种错误时，请使用make plain生成非压缩的脚本。
