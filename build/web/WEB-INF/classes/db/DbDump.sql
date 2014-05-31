
CREATE TABLE ROOT.GROUPS
(
	GROUPID INTEGER NOT NULL,
	USERID INTEGER NOT NULL,
	GROUPNAME VARCHAR(256) NOT NULL,
	GROUPDATACREATION DATE NOT NULL,
		PRIMARY KEY (GROUPID)
);

CREATE TABLE ROOT.GROUPUSER
(
	GROUPID INTEGER NOT NULL,
	USERID INTEGER NOT NULL,
	USERISADMIN BOOLEAN DEFAULT FALSE NOT NULL,
		PRIMARY KEY (GROUPID, USERID)
);

CREATE TABLE ROOT.INVITATION
(
	GROUPID INTEGER NOT NULL,
	USERID INTEGER NOT NULL,
		PRIMARY KEY (GROUPID, USERID)
);

CREATE TABLE ROOT.POST
(
	POSTID INTEGER NOT NULL,
	GROUPID INTEGER NOT NULL,
	USERID INTEGER NOT NULL,
	POSTDATACREATION TIMESTAMP NOT NULL,
	POSTTEXT CLOB,
		PRIMARY KEY (POSTID)
);

CREATE TABLE ROOT.POSTFILE
(
	POSTFILEID INTEGER NOT NULL,
        POSTID INTEGER NOT NULL,
	POSTFILEORIGINALFILENAME LONG VARCHAR,
	POSTFILEFILESYSTEMNAME LONG VARCHAR,
	POSTFILETYPE VARCHAR(1024),
		PRIMARY KEY (POSTFILEID)
);

CREATE TABLE ROOT.USERS
(
	USERID INTEGER NOT NULL,
	USERNAME VARCHAR(256) NOT NULL,
	USERPWD VARCHAR(256) NOT NULL,
	USERAVATAR LONG VARCHAR,
            PRIMARY KEY (USERID)
);


