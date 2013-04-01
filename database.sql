DROP TABLE IF EXISTS `app_users`;
CREATE TABLE `app_users` (
  `username` varchar(100) NOT NULL,
  `password` text NOT NULL,
  `firstname` text NOT NULL,
  `lastname` text NOT NULL,
  `email` text NOT NULL,
  `role` INT NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
INSERT INTO `app_users` VALUES ('root','root','Elian','ORIOU','eoriou@gmail.com', 3);
INSERT INTO `app_users` VALUES ('test','test','John','DOE','john@doe.com', 1);

