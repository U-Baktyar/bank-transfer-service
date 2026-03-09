DO $$
DECLARE
    sender_id BIGINT;
    receiver_id BIGINT;
    amount NUMERIC(19,2);
    sender_balance NUMERIC(19,2);
    receiver_balance NUMERIC(19,2);
    accounts_list BIGINT[];
    i INT;
BEGIN
    SELECT array_agg(id) INTO accounts_list FROM accounts WHERE status = 'ACTIVE';

    FOR i IN 1..100 LOOP
        sender_id := accounts_list[(random() * (array_length(accounts_list,1)-1) + 1)::int];
        LOOP
            receiver_id := accounts_list[(random() * (array_length(accounts_list,1)-1) + 1)::int];
            EXIT WHEN receiver_id <> sender_id;
        END LOOP;
        SELECT balance INTO sender_balance FROM accounts WHERE id = sender_id;
        SELECT balance INTO receiver_balance FROM accounts WHERE id = receiver_id;
        IF sender_balance < 1 THEN
            CONTINUE;
        END IF;
        amount := round((random() * (sender_balance/2) + 1)::numeric, 2);
        sender_balance := sender_balance - amount;
        receiver_balance := receiver_balance + amount;

        UPDATE accounts SET balance = sender_balance, version = version + 1 WHERE id = sender_id;
        UPDATE accounts SET balance = receiver_balance, version = version + 1 WHERE id = receiver_id;
        INSERT INTO transactions (
            sender_account_id, receiver_account_id, amount,
            sender_balance_after, receiver_balance_after, status
        ) VALUES (
            sender_id, receiver_id, amount, sender_balance, receiver_balance, 'SUCCESS'
        );
    END LOOP;
END $$;