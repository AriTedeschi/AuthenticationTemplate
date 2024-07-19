CREATE OR REPLACE FUNCTION generate_user_code() RETURNS TEXT AS $$
DECLARE
    user_identifier TEXT;
    isUnique BOOLEAN := FALSE;
    max_user_codes INTEGER := 100000;
BEGIN
    IF (SELECT COUNT(*) FROM TB_USER WHERE user_code LIKE '%-00000-00000') >= max_user_codes THEN
        RAISE EXCEPTION 'Achieved max user_code limit of %!', max_user_codes;
    END IF;

    WHILE NOT isUnique LOOP
        user_identifier := LPAD(CAST(FLOOR(RANDOM() * max_user_codes) AS TEXT), 5, '0');
        user_identifier := user_identifier || '-00000-00000';
        IF NOT EXISTS (SELECT 1 FROM TB_USER WHERE user_code = user_identifier) THEN
            isUnique := TRUE;
        END IF;
    END LOOP;

    RETURN user_identifier;
END;
$$ LANGUAGE plpgsql;