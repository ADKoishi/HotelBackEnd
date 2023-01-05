CREATE TABLE hotels(
	id SERIAL NOT NULL,

	address VARCHAR(800) NOT NULL,

    amenities VARCHAR(4) NOT NULL DEFAULT '0000',

	city_id INTEGER NOT NULL,

	longitude FLOAT NOT NULL,
	
	latitude FLOAT NOT NULL,
	
	name VARCHAR(400) NOT NULL,

	link VARCHAR DEFAULT NULL,

	contact_code VARCHAR(5) NOT NULL,

	contact VARCHAR(20) NOT NULL,

    description VARCHAR,

	default_tower INTEGER NOT NULL,

	scale INTEGER NOT NULL,

	priority INTEGER NOT NULL,

	picture BOOLEAN DEFAULT FALSE,

    points_avail BOOLEAN DEFAULT FALSE NOT NULL,

	deleted INTEGER DEFAULT 0 NOT NULL
		CONSTRAINT check_hotel_deleted
			CHECK(deleted IN (0,1))
	
) PARTITION BY RANGE (deleted);

CREATE TABLE hotels_avail PARTITION OF hotels FOR VALUES FROM (0) TO (1);

CREATE TABLE hotels_unavail PARTITION OF hotels FOR VALUES FROM (1) TO (2);

CREATE TABLE towers(
	id INTEGER NOT NULL,

	hotel_id INTEGER NOT NULL,

	name VARCHAR NOT NULL DEFAULT 'Main',

	lowest_floor INTEGER NOT NULL DEFAULT 2,

	highest_floor INTEGER NOT NULL

);


CREATE TABLE rooms(
	id SERIAL NOT NULL,
			
	floor INTEGER NOT NULL,
	
	hotel_id INTEGER NOT NULL,

    tower_id INTEGER NOT NULL,

	name VARCHAR NOT NULL,

	category INTEGER NOT NULL
			
);


CREATE TABLE categories(
	id SERIAL NOT NULL,
			
	hotel_id INTEGER NOT NULL,

	max_people INTEGER NOT NULL,

	max_children INTEGER NOT NULL,

	available_rates VARCHAR NOT NULL default '000',

    amenities VARCHAR(4) NOT NULL default '0000',

	accessible BOOLEAN DEFAULT FALSE,

	name VARCHAR(40) NOT NULL,
	
	price INTEGER NOT NULL,

	points INTEGER NOT NULL,

	description VARCHAR,

	picture BOOLEAN DEFAULT FALSE,

	prefix VARCHAR NOT NULL,

	currency VARCHAR NOT NULL
			
);


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

    mail VARCHAR(100) NOT NULL,

	firstname VARCHAR(40),

	lastname VARCHAR(40),
	
	enrolled_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	
	language VARCHAR(5) NOT NULL DEFAULT 'en-us',
	
	password VARCHAR(64) NOT NULL,
	
	avatar BOOLEAN DEFAULT FALSE,

	favorites VARCHAR NOT NULL,
	
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
	
	price FLOAT NOT NULL,
	
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
	code VARCHAR NOT NULL,

	ratio FLOAT NOT NULL

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