ewp_shell
=========
管理ewp运行时的shell环境，提供开箱即用的体验(LOL)。

### 动态更新ewp内存中的beam ###
make完之后会生成名为ewp_shell脚本，直接使用这一个脚本就可以了，操作如下：
* `./ewp_shell`启动，或者`./ewp_shell -debug`以调试模式启动；
* 脚本会首先列出当前运行的ewp node，输入运行中的ewp node的序号即可；
* 脚本接着会要求输入待加载的beam文件名或压缩包(支持zip和tar.gz格式)，如：path/to/file.beam或path/to/file.tar.gz；
* 自动导入beam，脚本结束；

### Features in Future ###
* ewp节点探测；
* erlang console接入；
* ewp集群节点状态管理；
* app的启动/关闭的支持；
* app部署；
* 远程操作；
* 脚本解释功能；
