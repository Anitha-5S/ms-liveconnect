-- schedule_demo definition

-- Drop table

-- DROP TABLE schedule_demo;

CREATE TABLE schedule_demo (
	c_store_name varchar(255) NOT NULL, -- Name of the Store
	c_owner_name varchar(255) NOT NULL, -- Name of the Owner
	c_mobile_no varchar(10) NOT NULL, -- Mobile Number of the Owner
	c_pincode varchar(6) NOT NULL, -- Pincode of the Store
	c_description varchar(1024) NOT NULL, -- Description of the Demo
	c_existing_customer varchar(1) NOT NULL, -- Y - Existing C2 customer; N - Non existing C2 customer
	n_demo_id bigserial NOT NULL, -- Demo Id
	n_created_by int8 NULL, -- Id of user who created
	t_created_at timestamp NULL, -- record creation time
	n_last_updated_by int8 NULL, -- Id of user who recenty updated
	t_last_updated_at timestamp NULL, -- record updated time
	CONSTRAINT schedule_demo_pk PRIMARY KEY (n_demo_id)
);

-- Column comments

COMMENT ON COLUMN schedule_demo.c_store_name IS 'Name of the Store';
COMMENT ON COLUMN schedule_demo.c_owner_name IS 'Name of the Owner';
COMMENT ON COLUMN schedule_demo.c_mobile_no IS 'Mobile Number of the Owner';
COMMENT ON COLUMN schedule_demo.c_pincode IS 'Pincode of the Store';
COMMENT ON COLUMN schedule_demo.c_description IS 'Description of the Demo';
COMMENT ON COLUMN schedule_demo.c_existing_customer IS 'Y - Existing C2 customer; N - Non existing C2 customer';
COMMENT ON COLUMN schedule_demo.n_demo_id IS 'Demo Id';
COMMENT ON COLUMN schedule_demo.n_created_by IS 'Id of user who created';
COMMENT ON COLUMN schedule_demo.t_created_at IS 'record creation time';
COMMENT ON COLUMN schedule_demo.n_last_updated_by IS 'Id of user who recenty updated';
COMMENT ON COLUMN schedule_demo.t_last_updated_at IS 'record updated time';