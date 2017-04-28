CREATE TABLE `yy_city` (
`id` int(11) NOT NULL AUTO_INCREMENT COMMENT '城市编号',
`province_id` int(11) DEFAULT NULL COMMENT '省份编号',
`city_name` varchar(25) DEFAULT NULL COMMENT '城市名称',
`description` varchar(25) DEFAULT NULL COMMENT '描述',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '城市列表';