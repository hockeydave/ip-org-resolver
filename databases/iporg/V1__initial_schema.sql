CREATE TABLE org_type (
                        id serial PRIMARY KEY,
                        name VARCHAR(255) UNIQUE);

CREATE TABLE org (
                        id serial PRIMARY KEY,
                        name VARCHAR(255) UNIQUE,
                        org_type_id INT REFERENCES org_type(id),
                        created_at TIMESTAMP without TIME ZONE DEFAULT now(),
                        updated_at TIMESTAMP without TIME ZONE DEFAULT now()
);

CREATE TABLE org_ip (
                          id serial PRIMARY KEY,
                          start_block_ip  BIGINT,
                          end_block_ip BIGINT,
                          org_id INT REFERENCES org(id),
                          created_at TIMESTAMP without TIME ZONE DEFAULT now(),
                          updated_at TIMESTAMP without time zone default now()
);

INSERT INTO org_type(name) values ('MOBILE_ISP');
INSERT INTO org_type(name) values ('RESIDENTIAL_ISP');
INSERT INTO org_type(name) values ('CLOUD');
INSERT INTO org_type(name) values ('CORPORATE');
INSERT INTO org(name, org_type_id) values ('Charter Communications Inc', 1);
INSERT INTO org(name, org_type_id) values ('Verizon Business (MCICS)', 1);
INSERT INTO org(name, org_type_id) values ('T-Mobile USA, Inc. (TMOBI)', 1);



