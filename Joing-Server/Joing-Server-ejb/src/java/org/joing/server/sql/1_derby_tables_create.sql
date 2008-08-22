-- ********************************************************************************************************************
--   Join'g - SERVER - TABLES DEFINITION
-- ********************************************************************************************************************

-- First step: to drop all tables (if any)

DROP TABLE USERS_WITH_APPS;
DROP TABLE APP_GROUP_DESCRIPTIONS;
DROP TABLE APPS_WITH_GROUPS;
DROP TABLE FILES;
DROP TABLE SESSIONS;
DROP TABLE APP_GROUPS;
DROP TABLE APPLICATIONS;
DROP TABLE USERS;
DROP TABLE LOCALES;

-- ********************************************************************************************************************

CREATE TABLE USERS(               -- Can't be named 'USER' because it is a SQL reserved word
   PRIMARY KEY (ACCOUNT),
   ACCOUNT     VARCHAR(128) NOT NULL ,   -- Identifies uniquely the user in all Joing communities (<account>@<community>.<domain>)
   PASSWORD    VARCHAR(32)  NOT NULL ,
   EMAIL       VARCHAR(64)           ,   -- In case that the community don't offer an email based on ACCOUNT
   FIRST_NAME  VARCHAR(48)           ,
   SECOND_NAME VARCHAR(48)           ,
   ID_LOCALE   INT                   ,   -- Language
   IS_MALE     SMALLINT     DEFAULT 1,   -- 0 == Female, 1 == Male
   QUOTA       INT          DEFAULT 0 ); -- Max disk quota (in Kb). 0 == no limit

--CREATE TABLE USER_GROUPS(cc         -- User groups (to make administration tasks easier)
--   PRIMARY KEY (ID_USER_GROUP),
--   ID_USER_GROUP INT GENERATED ALWAYS AS IDENTITY,
--   DESCRIPTION   VARCHAR(128) );

--CREATE TABLE USERS_WITH_GROUPS(   -- Many-To-Many: which users belong to which group
--   PRIMARY KEY (ACCOUNT, ID_USER_GROUP), 
--   ACCOUNT       VARCHAR(32) NOT NULL,
--   ID_USER_GROUP INT         NOT NULL );

CREATE TABLE USERS_WITH_APPS(               -- Many-To-Many: which apps are available to the user
   PRIMARY KEY (ACCOUNT, ID_APPLICATION)  , -- and which will be shown in the menu (installed == true)
   ACCOUNT        VARCHAR(128) NOT NULL   ,
   ID_APPLICATION INT          NOT NULL   ,
   ALLOW_REMOTE   SMALLINT     DEFAULT 0 ); -- Is this user allowed to run this app in the server?

-- ********************************************************************************************************************

CREATE TABLE APPLICATIONS(
   PRIMARY KEY (ID_APPLICATION),
   ID_APPLICATION INT GENERATED ALWAYS AS IDENTITY,   
   EXTRA_PATH     VARCHAR(255),             -- From applications dir (defined in Constant.sAPP_DIR)
   EXECUTABLE     VARCHAR(255) NOT NULL );  -- Normally a .jar or a .class, but could be a JRuby, Groovy, ... file

CREATE TABLE LOCALES(
   PRIMARY KEY (ID_LOCALE),
   ID_LOCALE INT GENERATED ALWAYS AS IDENTITY,
   IDIOM     VARCHAR(3) NOT NULL,               -- LOCALE language (LANGUAGE is a reserved SQL word)
   COUNTRY   VARCHAR(3) );

CREATE TABLE APP_GROUPS(          -- Application groups (categories)
   PRIMARY KEY (ID_APP_GROUP),
   ID_APP_GROUP INT            NOT NULL    ,   -- Refer to Common.dto.app.AppGroup.java
   ICON_PIXEL   VARCHAR(4096)  FOR BIT DATA,   -- A PNG (24x24) image up to 16Kb
   ICON_VECTOR  VARCHAR(16384) FOR BIT DATA ); -- A SVGZ (compresed) image up to 32Kb

CREATE TABLE APP_GROUP_DESCRIPTIONS(    -- Application Group name (descriptive) in different languages
   PRIMARY KEY (ID_APP_GROUP, ID_LOCALE),
   ID_APP_GROUP INT          NOT NULL,
   ID_LOCALE    INT          NOT NULL,  -- Language (as in java.util.Locale)
   GROUP_NAME   VARCHAR(64)  NOT NULL,  -- Short name
   DESCRIPTION  VARCHAR(255)        );  -- Long description

CREATE TABLE APPS_WITH_GROUPS(    -- Many-To-Many: Which apps belong to which groups
   PRIMARY KEY (ID_APPLICATION, ID_APP_GROUP), -- (to be grouped in the menu: categories)
   ID_APPLICATION INT NOT NULL,
   ID_APP_GROUP   INT NOT NULL );

-- ********************************************************************************************************************

CREATE TABLE FILES(               -- 'FILE' is a reserved SQL word 
   PRIMARY KEY (ID_FILE),
   ID_FILE         INT GENERATED ALWAYS AS IDENTITY, -- Real file name in real (native) FS
   ID_ORIGINAL     INT                    ,  -- In case it is a link: Which file is the original?  (null == none)
   ACCOUNT         VARCHAR(128)  NOT NULL ,  -- User that has this file or link in his/her disk-space (in the from <account>@<community>.<domain>)
   OWNER           VARCHAR(128)  NOT NULL ,  -- User that owns this file or link (in the from <account>@<joing_name>.<domain>)
   FILE_PATH       VARCHAR(4096)          ,  -- Accumulated path excluding file name (used mainly to search)
   FILE_NAME       VARCHAR(255)           ,  -- NULL when ID_ORIGINAL != null
   LOCKED_BY       VARCHAR(32)            ,  -- The account who requested to lock the file or null when unlocked
   IS_DIR          SMALLINT      DEFAULT 0,  -- 0 == It is a file, 1 == It is a directory
   IS_HIDDEN       SMALLINT      DEFAULT 0,  -- Hidden or not
   IS_PUBLIC       SMALLINT      DEFAULT 1,  -- Allow other users to know this file exist (v.g. can be copied)
   IS_READABLE     SMALLINT      DEFAULT 1,  -- Contents can be shown
   IS_MODIFIABLE   SMALLINT      DEFAULT 1,  -- Contents can be changed
   IS_DELETEABLE   SMALLINT      DEFAULT 1,  -- File can be deleted
   IS_EXECUTABLE   SMALLINT      DEFAULT 0,  -- It is an executable file
   IS_DUPLICABLE   SMALLINT      DEFAULT 1,  -- Copies of this file are allowed (by the owner and other users), otherwise only a link is allowed
   IS_ALTERABLE    SMALLINT      DEFAULT 1,  -- Allow other users than owner to change its attributes and contents
   IS_IN_TRASHCAN  SMALLINT      DEFAULT 0,  -- Is the file in the trash can?
   CREATED         TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,  -- When was created
   MODIFIED        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,  -- Last time it was modified
   ACCESSED        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,  -- Last time it was accessed
   NOTES           VARCHAR(2048)                         );

-- TODO: Hay que crear una tabla FILE_TYPES algo así:
--CREATE TABLE FILE_TYPES(
--   ID_FILE_TYPE INT GENERATED ALWAYS AS IDENTITY,
--   ID_FILE      INT            NOT NULL    ,
--   MIME_TYPE    VARCHAR(32)                ,
--   ICON_PIXEL   VARCHAR(4096)  FOR BIT DATA,    -- A PNG (24x24) image up to 4Kb
--   ICON_VECTOR  VARCHAR(16384) FOR BIT DATA );  -- A SVGZ (compresed) image up to 16Kb

-- ********************************************************************************************************************

CREATE TABLE SESSIONS(
   PRIMARY KEY (ID_SESSION),
   ID_SESSION VARCHAR(32)  NOT NULL,                    -- sSessionId
   ACCOUNT    VARCHAR(128) NOT NULL,                    -- User Id
   CREATED    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,   -- Time when the session was created
   ACCESSED   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ); -- Last time the session had activity

-- *****************************************   EOF  *******************************************************************