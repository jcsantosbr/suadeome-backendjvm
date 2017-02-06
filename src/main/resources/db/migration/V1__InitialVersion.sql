CREATE TABLE users (
  id         BIGINT NOT NULL,
  created_at TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE services (
  id              BIGINT       NOT NULL,
  normalized_name VARCHAR(100) NOT NULL,
  original_name   VARCHAR(200) NOT NULL,
  created_by      BIGINT       NOT NULL,
  created_at      TIMESTAMP(6) WITHOUT TIME ZONE,
  CONSTRAINT pk_services PRIMARY KEY (id),
  CONSTRAINT fk_created_by_to_users FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE professionals (
  id         BIGINT        NOT NULL,
  name       VARCHAR(1000) NOT NULL,
  service_id BIGINT        NOT NULL,
  phone      VARCHAR(100)  NOT NULL,
  created_by BIGINT        NOT NULL,
  created_at TIMESTAMP(6) WITHOUT TIME ZONE,
  CONSTRAINT pk_professionals PRIMARY KEY (id),
  CONSTRAINT fk_service_id_to_services FOREIGN KEY (service_id) REFERENCES services (id),
  CONSTRAINT fk_created_by_to_users FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE recommendations (
  recommender     BIGINT        NOT NULL,
  professional_id BIGINT        NOT NULL,
  grade           INT           NOT NULL,
  comments        VARCHAR(1000) NOT NULL,
  created_by      BIGINT        NOT NULL,
  created_at      TIMESTAMP(6) WITHOUT TIME ZONE,
  CONSTRAINT pk_recommendations PRIMARY KEY (professional_id, recommender),
  CONSTRAINT fk_recommender_to_users FOREIGN KEY (created_by) REFERENCES users (id),
  CONSTRAINT fk_professional_id FOREIGN KEY (professional_id) REFERENCES professionals (id),
  CONSTRAINT fk_created_by_to_users FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE contacts (
  user_id    BIGINT NOT NULL,
  contact_id BIGINT NOT NULL,
  created_by BIGINT NOT NULL,
  created_at TIMESTAMP(6) WITHOUT TIME ZONE,
  CONSTRAINT pk_contacts PRIMARY KEY (user_id, contact_id),
  CONSTRAINT fk_user_id_to_users FOREIGN KEY (user_id) REFERENCES users (id),
  CONSTRAINT fk_contact_id_to_users FOREIGN KEY (contact_id) REFERENCES users (id),
  CONSTRAINT fk_created_by_to_users FOREIGN KEY (created_by) REFERENCES users (id)
);



