# FastInflate

编译期时解析布局xml文件，将LayoutInflater中的反射实例化转成普通的实例化，以此来提高布局加载的性能

经测试，越能提升1.5倍布局加载的速度

**目前不支持含有`include`，`merge`等标签的布局加载**