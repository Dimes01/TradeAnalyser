CREATE OR REPLACE VIEW latest_analysis AS
SELECT a.*
FROM public.analysis a
INNER JOIN (
    SELECT account_id, securities_uid, MAX(date_to) AS max_date_to
    FROM public.analysis
    GROUP BY account_id, securities_uid
) b
ON a.account_id = b.account_id AND a.securities_uid = b.securities_uid AND a.date_to = b.max_date_to;
