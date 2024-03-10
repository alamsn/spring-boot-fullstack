UPDATE customer
SET gender =
        CASE
            WHEN id % 2 = 1 THEN 'MALE'
            WHEN id % 2 = 0 THEN 'FEMALE'
            ELSE gender
            END
WHERE gender IS NOT NULL;