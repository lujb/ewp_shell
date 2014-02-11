ewp_shell
=========
管理ewp运行时的shell环境，提供开箱即用的体验(LOL)。

### 脚本生成 ###
在*nix环境下，make直接生成ewp_shell脚本:
```sh
make
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

### 动态更新ewp内存中的beam ###
当前只支持向sname的ewp节点导入beam。

* 运行脚本；
* 如果当前运行有多个ewp节点，脚本将列出所有节点，并要求用户选择一个节点；
* 接着脚本会列出所有ebin路径，并要求用户选择一个ebin路径用来保存后续导入新增的beam(不是新增的则直接覆盖)；
* 脚本接着会要求输入待加载的beam文件名或压缩包(支持zip和tar.gz格式)，如：path/to/file.beam或path/to/file.tar.gz；
* 自动导入beam，回显统计数量；
* 脚本结束；

示例：
```
kingbo@caught:~/src/gitrepo/ewp_shell$ ./ewp_shell 
current running ewp nodes:
--------------------------
1) ebank@caught
2) ewp@caught
选择一个将导入beam的ewp节点(输入数字1~2): 1

You choose ebank@caught.

当前在节点ebank@caught中的ebin路径有:
--------------------------
1) /usr/local/lib/yaws/examples/ebin
2) /home/kingbo/src/rytong/emp.proj/ebank/../ewp/ebin
3) .
4) /usr/local/lib/yaws/ebin
5) /home/kingbo/src/rytong/emp.proj/ewp/ebin
6) /home/kingbo/src/rytong/emp.proj/ebank/ebin
选择一个ebin路径用以存放新增的beam(输入数字1~6): 6

You choose /home/kingbo/src/rytong/emp.proj/ebank/ebin.

请输入待导入的beam文件路径(支持单个beam文件或zip/tgz压缩文件): /home/kingbo/src/rytong/emp.proj/ebank/tmp/tmp.zip
[INFO] 没有新增的beam.
[INFO] 替换以下beam: abc.beam ebank_ass.beam
Done!
```

### Features in Future ###
* ewp节点探测；
* erlang console接入；
* ewp集群节点状态管理；
* app的启动/关闭的支持；
* app部署；
* 远程操作；
* 脚本解释功能；
