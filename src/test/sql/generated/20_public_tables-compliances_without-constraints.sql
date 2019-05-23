SET statement_timeout = 0;
SET lock_timeout = 0;
-- SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
-- SET row_security = off;
SET default_tablespace = '';
SET default_with_oids = false;
CREATE TABLE public.compliance_check_blocks (
    id bigint NOT NULL,
    name character varying,
    condition_attributes shared_extensions.hstore,
    compliance_check_set_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
CREATE SEQUENCE public.compliance_check_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.compliance_check_blocks_id_seq OWNED BY public.compliance_check_blocks.id;
CREATE TABLE public.compliance_check_messages (
    id bigint NOT NULL,
    compliance_check_id bigint,
    compliance_check_resource_id bigint,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    resource_attributes shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    status character varying,
    compliance_check_set_id bigint
);
CREATE SEQUENCE public.compliance_check_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.compliance_check_messages_id_seq OWNED BY public.compliance_check_messages.id;
CREATE TABLE public.compliance_check_resources (
    id bigint NOT NULL,
    status character varying,
    name character varying,
    resource_type character varying,
    reference character varying,
    metrics shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    compliance_check_set_id bigint
);
CREATE SEQUENCE public.compliance_check_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.compliance_check_resources_id_seq OWNED BY public.compliance_check_resources.id;
CREATE TABLE public.compliance_check_sets (
    id bigint NOT NULL,
    referential_id bigint,
    compliance_control_set_id bigint,
    workbench_id bigint,
    status character varying,
    parent_id bigint,
    parent_type character varying,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    current_step_id character varying,
    current_step_progress double precision,
    name character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    notified_parent_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
CREATE SEQUENCE public.compliance_check_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.compliance_check_sets_id_seq OWNED BY public.compliance_check_sets.id;
CREATE TABLE public.compliance_checks (
    id bigint NOT NULL,
    compliance_check_set_id bigint,
    compliance_check_block_id bigint,
    type character varying,
    control_attributes json,
    name character varying,
    code character varying,
    criticity character varying,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    origin_code character varying,
    compliance_control_name character varying
);
CREATE SEQUENCE public.compliance_checks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER SEQUENCE public.compliance_checks_id_seq OWNED BY public.compliance_checks.id;
ALTER TABLE ONLY public.compliance_check_blocks ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_blocks_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_check_messages ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_messages_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_check_resources ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_resources_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_check_sets ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_sets_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_checks ALTER COLUMN id SET DEFAULT nextval('public.compliance_checks_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_check_blocks    ADD CONSTRAINT compliance_check_blocks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.compliance_check_messages    ADD CONSTRAINT compliance_check_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.compliance_check_resources    ADD CONSTRAINT compliance_check_resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.compliance_check_sets    ADD CONSTRAINT compliance_check_sets_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.compliance_checks    ADD CONSTRAINT compliance_checks_pkey PRIMARY KEY (id);
CREATE INDEX index_compliance_check_blocks_on_compliance_check_set_id ON public.compliance_check_blocks USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_check_messages_on_compliance_check_id ON public.compliance_check_messages USING btree (compliance_check_id);
CREATE INDEX index_compliance_check_messages_on_compliance_check_resource_id ON public.compliance_check_messages USING btree (compliance_check_resource_id);
CREATE INDEX index_compliance_check_messages_on_compliance_check_set_id ON public.compliance_check_messages USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_check_resources_on_compliance_check_set_id ON public.compliance_check_resources USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_check_sets_on_compliance_control_set_id ON public.compliance_check_sets USING btree (compliance_control_set_id);
CREATE INDEX index_compliance_check_sets_on_parent_type_and_parent_id ON public.compliance_check_sets USING btree (parent_type, parent_id);
CREATE INDEX index_compliance_check_sets_on_referential_id ON public.compliance_check_sets USING btree (referential_id);
CREATE INDEX index_compliance_check_sets_on_workbench_id ON public.compliance_check_sets USING btree (workbench_id);
CREATE INDEX index_compliance_checks_on_compliance_check_block_id ON public.compliance_checks USING btree (compliance_check_block_id);
CREATE INDEX index_compliance_checks_on_compliance_check_set_id ON public.compliance_checks USING btree (compliance_check_set_id);
