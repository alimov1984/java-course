
CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL,
    is_locked boolean NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS public.limits_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS public.limits
(
    id bigint NOT NULL DEFAULT nextval('limits_id_seq'::regclass),
    row_date timestamp with time zone NOT NULL,
    transaction_id uuid NOT NULL,
    transaction_date timestamp with time zone NOT NULL,
    user_id bigint NOT NULL,
    sum numeric NOT NULL,
    CONSTRAINT limits_pkey PRIMARY KEY (id),
    CONSTRAINT limits_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

