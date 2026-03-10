SET TIME ZONE 'Asia/Bishkek';

-- 1) ТОП-5 счетов по количеству транзакций за последний месяц
WITH all_activity AS (

    SELECT sender_account_id AS account_id
    FROM transactions
    WHERE created_at >= CURRENT_DATE - INTERVAL '30 days'

    UNION ALL

    SELECT receiver_account_id
    FROM transactions
    WHERE created_at >= CURRENT_DATE - INTERVAL '30 days'

),

top_accounts AS (
    SELECT account_id
    FROM all_activity
    GROUP BY account_id
    ORDER BY COUNT(*) DESC
    LIMIT 5
)

SELECT * FROM accounts WHERE id IN (SELECT account_id FROM top_accounts);


-- 2) Общая сумма переводов за период
SELECT SUM(amount) AS total_transfers FROM transactions
WHERE created_at BETWEEN '2026-01-01' AND '2026-03-31';


-- 3) Счета с отрицательным балансом
SELECT * FROM accounts WHERE balance < 0;


-- 4) Оптимизация запроса
CREATE INDEX idx_accounts_account_no ON accounts(account_no);

SELECT * FROM accounts WHERE account_no = 'ACC00000000000000004';