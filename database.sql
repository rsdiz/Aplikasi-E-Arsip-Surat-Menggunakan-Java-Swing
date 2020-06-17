/*
MySQL: Database - surat_masuk_keluar
Created by: ptrakhsa
************************************
*/

CREATE DATABASE IF NOT EXISTS `surat_masuk_keluar`;

USE `surat_masuk_keluar`;

/*Table structure for table `bidang` */

DROP TABLE IF EXISTS `bidang`;

CREATE TABLE `bidang` (
  `id_bidang` INT NOT NULL,
  `nama_bidang` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_bidang`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Data for the table `bidang` */

INSERT  INTO `bidang`(`id_bidang`,`nama_bidang`)
VALUES 
(2,'Sekretaris'),
(2019001,'Kepala dinas pariwisata'),
(2020001,'Administrasi'),
(2020002,'Kegiatan'),
(2020003,'Sarana');

/*Table structure for table `disposisi` */

DROP TABLE IF EXISTS `disposisi`;

CREATE TABLE `disposisi` (
  `id_disposisi` INT NOT NULL AUTO_INCREMENT,
  `id_surat_masuk` INT NOT NULL,
  `id_bidang` INT NOT NULL,
  `no_srt_disposisi` VARCHAR(100) NOT NULL,
  `perihal_surat` VARCHAR(100) NOT NULL,
  `file_disposisi` BLOB,
  `keterangan` ENUM('disposisi','belum disposisi') DEFAULT NULL,
  PRIMARY KEY (`id_disposisi`),
  KEY `id_surat_masuk` (`id_surat_masuk`,`id_bidang`),
  KEY `id_bidang` (`id_bidang`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Table structure for table `petugas_pns` */

DROP TABLE IF EXISTS `petugas_pns`;

CREATE TABLE `petugas_pns` (
  `nip` VARCHAR(18) NOT NULL,
  `nama` VARCHAR(100) NOT NULL,
  `jabatan` VARCHAR(100) NOT NULL,
  `id_bidang` INT NOT NULL,
  `id_user` INT NOT NULL,
  UNIQUE KEY `nip` (`nip`),
  KEY `id_bidang` (`id_bidang`),
  KEY `id_user` (`id_user`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Data for the table `petugas_pns` */

INSERT  INTO `petugas_pns`(`nip`,`nama`,`jabatan`,`id_bidang`,`id_user`) VALUES 
('200005242020011001','Muhammad Rosyid Izzulkhaq','Direktur Utama',2020001,1),
('200004212020021002','Taufik Ismail','System Analyst',2020003,2),
('200004112020012003','Putri Akhsadini','Quality Assurance',2020002,3),
('200008182020022004','Elvina Pandan Manik','Data Quality Manager',2020003,4);

/*Table structure for table `surat_keluar` */

DROP TABLE IF EXISTS `surat_keluar`;

CREATE TABLE `surat_keluar` (
  `id_surat_keluar` INT NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL,
  `no_agenda` VARCHAR(100) NOT NULL,
  `no_surat` VARCHAR(100) NOT NULL,
  `tgl_surat` DATE NOT NULL,
  `lampiran` VARCHAR(100) NOT NULL,
  `perihal` VARCHAR(100) NOT NULL,
  `tembusan` VARCHAR(100) NOT NULL,
  `tujuan_surat` VARCHAR(100) NOT NULL,
  `file_surat` BLOB,
  PRIMARY KEY (`id_surat_keluar`),
  KEY `id_user` (`id_user`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Table structure for table `surat_masuk` */

DROP TABLE IF EXISTS `surat_masuk`;

CREATE TABLE `surat_masuk` (
  `id_surat_masuk` INT NOT NULL AUTO_INCREMENT,
  `id_user` INT NOT NULL,
  `no_agenda` VARCHAR(100) NOT NULL,
  `asal_surat` VARCHAR(100) NOT NULL,
  `tgl_diterima` DATE NOT NULL,
  `tgl_surat` DATE NOT NULL,
  `no_surat` VARCHAR(100) NOT NULL,
  `lampiran` VARCHAR(100) NOT NULL,
  `perihal` VARCHAR(100) NOT NULL,
  `tembusan` VARCHAR(100) NOT NULL,
  `file_surat` BLOB,
  `status` ENUM('dibaca','belum dibaca') DEFAULT NULL,
  PRIMARY KEY (`id_surat_masuk`),
  KEY `id_user` (`id_user`)
) ENGINE=INNODB DEFAULT CHARSET=latin1;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `level` ENUM('admin','petugas') DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `unique_username` (`username`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

INSERT  INTO `user`(`id_user`,`username`,`password`,`level`) VALUES 
(1,'rosyidiz','UAi+NccCGjzkcddfZXOpt2unD7zZFU4o0r1Kecv2t3o=','admin'),
(2,'taufik21','pnWAuHDwxa7zrGzAFLT7BTzGJ+a9uZZfx7912ry4znY=','petugas'),
(3,'ptrakhsa','mfTYNaS2xHlu1lCQ3IWGk++4b9t//jNyMMk+ki8Jx6Y=','petugas'),
(4,'elvina','Nnp46lL5+uQkLkOITZ+bzPapacGw7olo4+KaoDyFByM=','petugas');

/* Constraint Reference */

ALTER TABLE `disposisi`
ADD CONSTRAINT `disposisi_ibfk_1` FOREIGN KEY (`id_surat_masuk`) REFERENCES `surat_masuk` (`id_surat_masuk`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `disposisi_ibfk_2` FOREIGN KEY (`id_bidang`) REFERENCES `bidang` (`id_bidang`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `petugas_pns`
ADD CONSTRAINT `petugas_pns_ibfk_1` FOREIGN KEY (`id_bidang`) REFERENCES `bidang` (`id_bidang`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `petugas_pns_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `surat_keluar`
ADD CONSTRAINT `surat_keluar_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `surat_masuk`
ADD CONSTRAINT `surat_masuk_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON DELETE CASCADE ON UPDATE CASCADE;

/* Procedure structure for procedure `insert_petugas` (by @rsdiz)*/

DROP PROCEDURE IF EXISTS `insert_petugas`;

DELIMITER $$
CREATE PROCEDURE `insert_petugas`(
	in_username VARCHAR(100),
	in_password VARCHAR(100),
	in_level ENUM('admin','petugas'),
	in_nip VARCHAR(18),
	in_nama VARCHAR(100),
	in_jabatan VARCHAR(100),
	in_nama_bidang VARCHAR(100))
BEGIN
	DECLARE idbidang INT;
	DECLARE iduser INT;
	INSERT INTO `user` (username, PASSWORD, LEVEL)
	VALUES (in_username, in_password, in_level);
	SELECT id_user INTO iduser FROM `user` WHERE username = in_username;
	SELECT id_bidang INTO idbidang FROM bidang WHERE nama_bidang = in_nama_bidang;
	INSERT INTO petugas_pns (id_bidang, id_user, nip, nama, jabatan)
	VALUES (
	  idbidang,
	  iduser,
	  in_nip,
	  in_nama,
	  in_jabatan);
END $$
DELIMITER ;

/* Procedure structure for procedure `update_petugas` (by @rsdiz)*/

DROP PROCEDURE IF EXISTS  `update_petugas`;

DELIMITER $$

CREATE PROCEDURE `update_petugas`(
	old_nip VARCHAR(18),
	new_nip VARCHAR(18),
	new_nama VARCHAR(100),
	new_jabatan VARCHAR(100),
	new_username VARCHAR(100),
	new_password VARCHAR(100),
	new_level ENUM('admin','petugas'),
	new_nama_bidang VARCHAR(100))
BEGIN
	DECLARE iduser INT;
	DECLARE idbidang INT;
	SELECT id_user INTO iduser FROM USER INNER JOIN petugas_pns USING(id_user) WHERE nip = old_nip;
	SELECT id_bidang INTO idbidang FROM bidang WHERE nama_bidang = new_nama_bidang;
	UPDATE `user` SET username = new_username, PASSWORD = new_password, LEVEL = new_level WHERE id_user = iduser;
	UPDATE `petugas_pns` SET nip = new_nip, nama = new_nama, jabatan = new_jabatan, id_bidang = idbidang WHERE nip = old_nip;
END $$
DELIMITER ;

/* Procedure structure for procedure `delete_petugas` (by @rsdiz)*/

DROP PROCEDURE IF EXISTS  `delete_petugas`;

DELIMITER $$

CREATE PROCEDURE `delete_petugas`(key_nip VARCHAR(18))
BEGIN
	DECLARE iduser INT;
	SELECT id_user INTO iduser FROM `user` INNER JOIN petugas_pns USING(id_user) WHERE nip = key_nip;
	DELETE FROM petugas_pns WHERE nip = key_nip;
	DELETE FROM `user` WHERE id_user = iduser;
END $$
DELIMITER ;

/* Procedure structure for procedure `insert_bidang` */

DROP PROCEDURE IF EXISTS  `insert_bidang`;

DELIMITER $$
CREATE PROCEDURE insert_bidang(
	id_bidang INT,
	nama_bidang VARCHAR(100))
BEGIN
	INSERT INTO bidang VALUES (id_bidang, nama_bidang);
END $$
DELIMITER ;

/* Procedure structure for procedure `update_bidang` (by @rsdiz)*/

DROP PROCEDURE IF EXISTS  `update_bidang`;

DELIMITER $$
CREATE PROCEDURE update_bidang(
	old_id_bidang INT,
	new_id_bidang INT,
	new_nama_bidang VARCHAR(100))
BEGIN
	UPDATE `bidang` SET id_bidang = new_id_bidang, nama_bidang = new_nama_bidang WHERE id_bidang = old_id_bidang;
END $$
DELIMITER ;

/* Procedure structure for procedure `delete_bidang` (by @rsdiz)*/

DROP PROCEDURE IF EXISTS  `delete_bidang`;

DELIMITER $$
CREATE PROCEDURE delete_bidang(idkey INT)
BEGIN
	DELETE FROM bidang WHERE id_bidang = idkey;
END $$
DELIMITER ;

/*Table structure for table `info_petugas` */

DROP TABLE IF EXISTS `info_petugas`;

DROP VIEW IF EXISTS `info_petugas`;
DROP TABLE IF EXISTS `info_petugas`;

CREATE TABLE `info_petugas`(
 `nip` VARCHAR(18) ,
 `nama` VARCHAR(100) ,
 `jabatan` VARCHAR(100) ,
 `nama_bidang` VARCHAR(100) ,
 `username` VARCHAR(100) ,
 `password` VARCHAR(100) ,
 `level` ENUM('admin','petugas') 
);

/*View structure for view info_petugas */

DROP TABLE IF EXISTS `info_petugas`;
DROP VIEW IF EXISTS `info_petugas`;

CREATE VIEW `info_petugas` AS SELECT `petugas_pns`.`nip` AS `nip`,`petugas_pns`.`nama` AS `nama`,`petugas_pns`.`jabatan` AS `jabatan`,`bidang`.`nama_bidang` AS `nama_bidang`,`user`.`username` AS `username`,`user`.`password` AS `password`,`user`.`level` AS `level` FROM ((`petugas_pns` JOIN `bidang` ON((`petugas_pns`.`id_bidang` = `bidang`.`id_bidang`))) JOIN `user` ON((`petugas_pns`.`id_user` = `user`.`id_user`)));