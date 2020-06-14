/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 8.0.20 : Database - surat_masuk_keluar
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`surat_masuk_keluar` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `surat_masuk_keluar`;

/*Table structure for table `bidang` */

DROP TABLE IF EXISTS `bidang`;

CREATE TABLE `bidang` (
  `id_bidang` int NOT NULL,
  `nama_bidang` varchar(100) NOT NULL,
  PRIMARY KEY (`id_bidang`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `bidang` */

insert  into `bidang`(`id_bidang`,`nama_bidang`) values 
(2,'Sekretaris'),
(2019001,'Kepala dinas pariwisata'),
(2020001,'Administrasi'),
(2020002,'Kegiatan'),
(2020003,'Sarana');

/*Table structure for table `disposisi` */

DROP TABLE IF EXISTS `disposisi`;

CREATE TABLE `disposisi` (
  `id_disposisi` int NOT NULL AUTO_INCREMENT,
  `id_surat_masuk` int NOT NULL,
  `id_bidang` int NOT NULL,
  `no_srt_disposisi` varchar(100) NOT NULL,
  `perihal_surat` varchar(100) NOT NULL,
  `file_disposisi` blob,
  `keterangan` enum('disposisi','belum disposisi') DEFAULT NULL,
  PRIMARY KEY (`id_disposisi`),
  KEY `id_surat_masuk` (`id_surat_masuk`,`id_bidang`),
  KEY `id_bidang` (`id_bidang`),
  CONSTRAINT `disposisi_ibfk_1` FOREIGN KEY (`id_surat_masuk`) REFERENCES `surat_masuk` (`id_surat_masuk`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `disposisi_ibfk_2` FOREIGN KEY (`id_bidang`) REFERENCES `bidang` (`id_bidang`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `disposisi` */

/*Table structure for table `petugas_pns` */

DROP TABLE IF EXISTS `petugas_pns`;

CREATE TABLE `petugas_pns` (
  `nip` varchar(18) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `jabatan` varchar(100) NOT NULL,
  `id_bidang` int NOT NULL,
  `id_user` int NOT NULL,
  KEY `id_bidang` (`id_bidang`),
  KEY `id_user` (`id_user`),
  CONSTRAINT `petugas_pns_ibfk_1` FOREIGN KEY (`id_bidang`) REFERENCES `bidang` (`id_bidang`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `petugas_pns_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `petugas_pns` */

insert  into `petugas_pns`(`nip`,`nama`,`jabatan`,`id_bidang`,`id_user`) values 
('200005242020011001','Muhammad Rosyid Izzulkhaq','Direktur Utama',2020001,1),
('200004212020021002','Taufik Ismail','System Analyst',2020003,2),
('200004112020012003','Putri Akhsadini','Quality Assurance',2020002,3),
('200008182020022004','Elvina Pandan Manik','Data Quality Manager',2020003,4);

/*Table structure for table `surat_keluar` */

DROP TABLE IF EXISTS `surat_keluar`;

CREATE TABLE `surat_keluar` (
  `id_surat_keluar` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `no_agenda` varchar(100) NOT NULL,
  `no_surat` varchar(100) NOT NULL,
  `tgl_surat` date NOT NULL,
  `lampiran` varchar(100) NOT NULL,
  `perihal` varchar(100) NOT NULL,
  `tembusan` varchar(100) NOT NULL,
  `tujuan_surat` varchar(100) NOT NULL,
  `file_surat` blob,
  PRIMARY KEY (`id_surat_keluar`),
  KEY `id_user` (`id_user`),
  CONSTRAINT `surat_keluar_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `surat_keluar` */

/*Table structure for table `surat_masuk` */

DROP TABLE IF EXISTS `surat_masuk`;

CREATE TABLE `surat_masuk` (
  `id_surat_masuk` int NOT NULL AUTO_INCREMENT,
  `id_user` int NOT NULL,
  `no_agenda` varchar(100) NOT NULL,
  `asal_surat` varchar(100) NOT NULL,
  `tgl_diterima` date NOT NULL,
  `tgl_surat` date NOT NULL,
  `no_surat` varchar(100) NOT NULL,
  `lampiran` varchar(100) NOT NULL,
  `perihal` varchar(100) NOT NULL,
  `tembusan` varchar(100) NOT NULL,
  `file_surat` blob,
  `status` enum('dibaca','belum dibaca') DEFAULT NULL,
  PRIMARY KEY (`id_surat_masuk`),
  KEY `id_user` (`id_user`),
  CONSTRAINT `surat_masuk_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `surat_masuk` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id_user` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `level` enum('admin','petugas') DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `unique_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`id_user`,`username`,`password`,`level`) values 
(1,'rosyidiz','UAi+NccCGjzkcddfZXOpt2unD7zZFU4o0r1Kecv2t3o=','admin'),
(2,'taufik21','pnWAuHDwxa7zrGzAFLT7BTzGJ+a9uZZfx7912ry4znY=','petugas'),
(3,'ptrakhsa','mfTYNaS2xHlu1lCQ3IWGk++4b9t//jNyMMk+ki8Jx6Y=','petugas'),
(4,'elvina','Nnp46lL5+uQkLkOITZ+bzPapacGw7olo4+KaoDyFByM=','petugas');

/* Procedure structure for procedure `insert_petugas` */

/*!50003 DROP PROCEDURE IF EXISTS  `insert_petugas` */;

DELIMITER $$

/*!50003 CREATE PROCEDURE `insert_petugas`(
	in_username VARCHAR(100),
	in_password varchar(100),
	in_level enum('admin','petugas'),
	in_nip varchar(18),
	in_nama varchar(100),
	in_jabatan varchar(100),
	in_nama_bidang varchar(100))
BEGIN
	declare idbidang int;
	DECLARE iduser int;
	
	INSERT INTO `user` (username, password, level)
	VALUES (in_username, in_password, in_level);
	
	SELECT id_user into iduser FROM `user` WHERE username = in_username;
	
	SELECT id_bidang into idbidang FROM bidang WHERE nama_bidang = in_nama_bidang;
	
	INSERT INTO petugas_pns (id_bidang, id_user, nip, nama, jabatan)
	VALUES (
	  idbidang,
	  iduser,
	  in_nip,
	  in_nama,
	  in_jabatan);
END */$$
DELIMITER ;

/* Procedure structure for procedure `update_petugas` */

/*!50003 DROP PROCEDURE IF EXISTS  `update_petugas` */;

DELIMITER $$

/*!50003 CREATE PROCEDURE `update_petugas`(
	old_nip varchar(18),
	new_nip varchar(18),
	new_nama varchar(100),
	new_jabatan VARCHAR(100),
	new_username varchar(100),
	new_password varchar(100),
	new_level enum('admin','petugas'),
	new_nama_bidang varchar(100))
BEGIN
	DECLARE iduser int;
	DECLARE idbidang int;
	
	SELECT id_user INTO iduser FROM user inner join petugas_pns using(id_user) WHERE nip = old_nip;
	
	SELECT id_bidang INTO idbidang FROM bidang WHERE nama_bidang = new_nama_bidang;
	
	update `user` set username = new_username, PASSWORD = new_password, level = new_level WHERE id_user = iduser;
		
	update `petugas_pns` set nip = new_nip, nama = new_nama, jabatan = new_jabatan, id_bidang = idbidang WHERE nip = old_nip;
END */$$
DELIMITER ;

/* Procedure structure for procedure `delete_petugas` */

/*!50003 DROP PROCEDURE IF EXISTS  `delete_petugas` */;

DELIMITER $$

/*!50003 CREATE PROCEDURE `delete_petugas`(key_nip VARCHAR(18))
BEGIN
	DECLARE iduser int;

	SELECT id_user into iduser FROM `user` INNER JOIN petugas_pns USING(id_user) WHERE nip = key_nip;
	
	DELETE from petugas_pns WHERE nip = key_nip;
	
	DELETE FROM `user` WHERE id_user = iduser;
END */$$
DELIMITER ;

/*Table structure for table `info_petugas` */

DROP TABLE IF EXISTS `info_petugas`;

/*!50001 DROP VIEW IF EXISTS `info_petugas` */;
/*!50001 DROP TABLE IF EXISTS `info_petugas` */;

/*!50001 CREATE TABLE  `info_petugas`(
 `nip` varchar(18) ,
 `nama` varchar(100) ,
 `jabatan` varchar(100) ,
 `nama_bidang` varchar(100) ,
 `username` varchar(100) ,
 `password` varchar(100) ,
 `level` enum('admin','petugas') 
)*/;

/*View structure for view info_petugas */

/*!50001 DROP TABLE IF EXISTS `info_petugas` */;
/*!50001 DROP VIEW IF EXISTS `info_petugas` */;

/*!50001 CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `info_petugas` AS select `petugas_pns`.`nip` AS `nip`,`petugas_pns`.`nama` AS `nama`,`petugas_pns`.`jabatan` AS `jabatan`,`bidang`.`nama_bidang` AS `nama_bidang`,`user`.`username` AS `username`,`user`.`password` AS `password`,`user`.`level` AS `level` from ((`petugas_pns` join `bidang` on((`petugas_pns`.`id_bidang` = `bidang`.`id_bidang`))) join `user` on((`petugas_pns`.`id_user` = `user`.`id_user`))) */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
