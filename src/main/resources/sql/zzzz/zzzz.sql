-- 联系人表
CREATE TABLE `zz_contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `cert_id` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `mobile` varchar(20) DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='联系人';