CREATE TABLE hotels(
	id SERIAL NOT NULL,

	city_id INTEGER NOT NULL,

	longitude FLOAT NOT NULL,
	
	latitude FLOAT NOT NULL,
	
	name VARCHAR(400) NOT NULL,

	contact_code VARCHAR(5) NOT NULL,

	contact VARCHAR(20) NOT NULL,
	
	address VARCHAR(800) NOT NULL,

	scale INTEGER NOT NULL,

	priority INTEGER NOT NULL,

	picture BOOLEAN DEFAULT FALSE,
	
	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_hotel_deleted
			CHECK(deleted IN (0,1))
	
) PARTITION BY RANGE (deleted);

CREATE TABLE hotels_avail PARTITION OF hotels FOR VALUES FROM (0) TO (1);

CREATE TABLE hotels_unavail PARTITION OF hotels FOR VALUES FROM (1) TO (2);


CREATE TABLE floors(
	hotel_id INTEGER NOT NULL,
			
	number INTEGER NOT NULL,
	
	floor_plan BOOLEAN DEFAULT FALSE,
		
	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_floor_deleted
			CHECK(deleted IN (0,1))
	
) PARTITION BY RANGE (deleted);

CREATE TABLE floors_avail PARTITION OF floors FOR VALUES FROM (0) TO (1);

CREATE TABLE floors_unavail PARTITION OF floors FOR VALUES FROM (1) TO (2);


CREATE TABLE rooms(
	id SERIAL NOT NULL,
			
	floor_id INTEGER NOT NULL,
	
	hotel_id INTEGER NOT NULL,

	name INTEGER NOT NULL,

	accessibility BOOLEAN DEFAULT FALSE,

	max_people INTEGER NOT NULL,

	max_children INTEGER NOT NULL,

	category INTEGER NOT NULL,
	
	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_room_deleted
			CHECK(deleted IN (0,1))
			
) PARTITION BY RANGE (deleted);

CREATE TABLE rooms_avail PARTITION OF rooms FOR VALUES FROM (0) TO (1);

CREATE TABLE rooms_unavail PARTITION OF rooms FOR VALUES FROM (1) TO (2);


CREATE TABLE categories(
	id SERIAL NOT NULL,
			
	hotel_id INTEGER NOT NULL,

	name VARCHAR(40) NOT NULL,
	
	price INTEGER NOT NULL,

	points INTEGER NOT NULL,

	description VARCHAR,

	picture BOOLEAN DEFAULT FALSE,

	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_category_deleted
			CHECK(deleted IN (0,1))
			
) PARTITION BY RANGE (deleted);

CREATE TABLE categories_avail PARTITION OF categories FOR VALUES FROM (0) TO (1);

CREATE TABLE categories_unavail PARTITION OF categories FOR VALUES FROM (1) TO (2);


CREATE TABLE clip_path(
	clip_path_id SERIAL NOT NULL,
			
	room_id INTEGER NOT NULL,

	x_coordinates FLOAT NOT NULL,

	y_coordinates FLOAT NOT NULL
			
);


CREATE TABLE users (
	id SERIAL NOT NULL,

	role INTEGER DEFAULT 1 NOT NULL,

	labels VARCHAR(6) default '000000' NOT NULL,

	firstname VARCHAR(40),

	lastname VARCHAR(40),
	
	enrolled_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	
	language VARCHAR(5) NOT NULL DEFAULT 'en-us',
	
	password VARCHAR(64) NOT NULL,
	
	avatar BOOLEAN DEFAULT FALSE,
	
	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_user_deleted
			CHECK(deleted IN (0,1))
			
) PARTITION BY RANGE (deleted);

CREATE TABLE users_avail PARTITION OF users FOR VALUES FROM (0) TO (1);

CREATE TABLE users_unavail PARTITION OF users FOR VALUES FROM (1) TO (2);


CREATE TABLE customers (
	id INTEGER NOT NULL,
	
	name_prefix VARCHAR(10) DEFAULT NULL,
	name_suffix VARCHAR(10) DEFAULT NULL,
	
	phone_number VARCHAR(15) DEFAULT NULL,
	
	phone_head VARCHAR(5) DEFAULT NULL,
	
	mail VARCHAR(100) NOT NULL,
	
	birthday DATE DEFAULT NULL,

	gender VARCHAR(20) DEFAULT NULL,
	
	points INTEGER NOT NULL DEFAULT 0,
	
	accumulated INTEGER NOT NULL DEFAULT 0,
	
	country VARCHAR(20) DEFAULT NULL,
	
	last_visited INTEGER NOT NULL DEFAULT 0,
	
	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_customer_deleted
			CHECK(deleted IN (0,1))
			
) PARTITION BY RANGE (deleted);

CREATE TABLE customers_avail PARTITION OF customers FOR VALUES FROM (0) TO (1);

CREATE TABLE customers_unavail PARTITION OF customers FOR VALUES FROM (1) TO (2);


CREATE TABLE orders (
	order_number VARCHAR NOT NULL,
	
	user_id INTEGER NOT NULL,
	
	pice FLOAT NOT NULL,
	
	hotel_id INTEGER NOT NULL,

	room_id INTEGER NOT NULL,
	
	start_date DATE NOT NULL,

	end_date DATE NOT NULL,

	people INTEGER NOT NULL,

	children INTEGER NOT NULL,

	points INTEGER NOT NULL DEFAULT 0,
	
	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_order_deleted
			CHECK(deleted IN (0,1))
			
) PARTITION BY RANGE (deleted);

CREATE TABLE orders_avail PARTITION OF orders FOR VALUES FROM (0) TO (1);

CREATE TABLE orders_unavail PARTITION OF orders FOR VALUES FROM (1) TO (2);


CREATE TABLE reviews (
	id SERIAL NOT NULL,
			
	user_id INTEGER NOT NULL,

	order_number VARCHAR NOT NULL,
	
	language VARCHAR(5) NOT NULL DEFAULT 'en-us',
		
	title VARCHAR(400) NOT NULL,
		
	/*
		Review type:
		0 - normal review,
		1 - user guid,
	*/
	type INTEGER NOT NULL DEFAULT 0,
	
	stars INTEGER NOT NULL,
		
	post_date DATE NOT NULL DEFAULT CURRENT_DATE,
	
	edit_date DATE NOT NULL DEFAULT CURRENT_DATE,
	
	description VARCHAR (12800) NOT NULL,
	
	picture BOOLEAN NOT NULL DEFAULT FALSE,
	
	videos BOOLEAN NOT NULL DEFAULT FALSE,
	
	visible BOOLEAN DEFAULT FALSE NOT NULL,
	
	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_review_deleted
			CHECK(deleted IN (0,1))
			
) PARTITION BY RANGE (deleted);

CREATE TABLE reviews_avail PARTITION OF reviews FOR VALUES FROM (0) TO (1);

CREATE TABLE reviews_unavail PARTITION OF reviews FOR VALUES FROM (1) TO (2);


CREATE TABLE offers (
	id SERIAL NOT NULL,

	type INTEGER NOT NULL,

	value INTEGER NOT NULL,
			
	picture BOOLEAN DEFAULT FALSE	
);

CREATE TABLE offer_types (
	id SERIAL NOT NULL,

	name VARCHAR(20) DEFAULT 'percentage',

	description VARCHAR NOT NULL
);

CREATE TABLE offerings (
	offer_id INTEGER NOT NULL,

	offer_type VARCHAR(20) NOT NULL,

	offer_for INTEGER NOT NULL
);

CREATE TABLE notices (
	id SERIAL NOT NULL,
	
	tile VARCHAR NOT NULL,
	
	content VARCHAR NOT NULL,
	
	picture BOOLEAN DEFAULT FALSE,

	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_notice_deleted
			CHECK(deleted IN (0,1))
			
) PARTITION BY RANGE (deleted);

CREATE TABLE notices_avail PARTITION OF notices FOR VALUES FROM (0) TO (1);

CREATE TABLE notices_unavail PARTITION OF notices FOR VALUES FROM (1) TO (2);