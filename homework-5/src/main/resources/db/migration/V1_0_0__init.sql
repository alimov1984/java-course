
CREATE SEQUENCE IF NOT EXISTS public.product_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    OWNED BY products.id;

CREATE TABLE IF NOT EXISTS public.products
(
    id bigint NOT NULL DEFAULT nextval('product_id_seq'::regclass),
    account_number character varying(100) COLLATE pg_catalog."default" NOT NULL,
    balance numeric(50,4) NOT NULL,
    type character varying(100) COLLATE pg_catalog."default" NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT product_pkey PRIMARY KEY (id)
)