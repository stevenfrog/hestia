-- Please note that these changes are already merged with the full schema SQL file
-- You only need to execute this in case of incremental deployment

--------------------------------------------------------
--  DDL for Table USERFILTERRECORD_SHIPPOINT
--------------------------------------------------------

CREATE TABLE "USERFILTERRECORD_SHIPPOINT" (
  "USERFILTERRECORD_ID" NUMBER(19,0), 
  "SHIPPOINT_ID" NUMBER(19,0)
);

--------------------------------------------------------
--  Constraints for Table USERFILTERRECORD_SHIPPOINT
--------------------------------------------------------

ALTER TABLE "USERFILTERRECORD_SHIPPOINT" MODIFY ("USERFILTERRECORD_ID" NOT NULL ENABLE);
ALTER TABLE "USERFILTERRECORD_SHIPPOINT" MODIFY ("SHIPPOINT_ID" NOT NULL ENABLE);
ALTER TABLE "USERFILTERRECORD_SHIPPOINT" ADD PRIMARY KEY ("USERFILTERRECORD_ID", "SHIPPOINT_ID") ENABLE;

--------------------------------------------------------
--  DDL for Table USERFILTERRECORD_PRODUCT
--------------------------------------------------------

CREATE TABLE "USERFILTERRECORD_PRODUCT" (
  "USERFILTERRECORD_ID" NUMBER(19,0), 
  "PRODUCT_ID" NUMBER(19,0)
);

--------------------------------------------------------
--  Constraints for Table USERFILTERRECORD_SHIPPOINT
--------------------------------------------------------

ALTER TABLE "USERFILTERRECORD_PRODUCT" MODIFY ("USERFILTERRECORD_ID" NOT NULL ENABLE);
ALTER TABLE "USERFILTERRECORD_PRODUCT" MODIFY ("PRODUCT_ID" NOT NULL ENABLE);
ALTER TABLE "USERFILTERRECORD_PRODUCT" ADD PRIMARY KEY ("USERFILTERRECORD_ID", "PRODUCT_ID") ENABLE;

   
ALTER TABLE "SHIPPOINT" ADD "TYPE_ID" NUMBER(19,0) NULL;

ALTER TABLE "USER_TABLE" ADD "LAST_LOGIN_TS" TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP; 