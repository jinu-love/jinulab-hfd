with recursive month_generator as (
	select cast('20250701' as date) as month_start
	union all
	select month_start + interval 1 month from month_generator where month_start < '20250901'
) 
select u.email
, '(주)아이셋디엑스' as company
, u.name
	, year(mg.month_start) as year
	, month(mg.month_start) as month
	,   os.*
from users u cross join month_generator mg left outer join (
	select
	year(oi.overtime_date) as year
	,month(oi.overtime_date) as month
	,sum(oi.overtime_hours) as total
	,max(case when day(oi.overtime_date) = 1 then oi.overtime_hours when day(doi.day_off_date) = 1 then '해피휴가' else 0 end ) as d1
	,max(case when day(oi.overtime_date) = 2 then oi.overtime_hours when day(doi.day_off_date) = 2 then '해피휴가' else 0 end ) as d2
	,max(case when day(oi.overtime_date) = 3 then oi.overtime_hours when day(doi.day_off_date) = 3 then '해피휴가' else 0 end ) as d3
	,max(case when day(oi.overtime_date) = 4 then oi.overtime_hours when day(doi.day_off_date) = 4 then '해피휴가' else 0 end ) as d4
	,max(case when day(oi.overtime_date) = 5 then oi.overtime_hours when day(doi.day_off_date) = 5 then '해피휴가' else 0 end ) as d5
	,max(case when day(oi.overtime_date) = 6 then oi.overtime_hours when day(doi.day_off_date) = 6 then '해피휴가' else 0 end ) as d6
	,max(case when day(oi.overtime_date) = 7 then oi.overtime_hours when day(doi.day_off_date) = 7 then '해피휴가' else 0 end ) as d7
	,max(case when day(oi.overtime_date) = 8 then oi.overtime_hours when day(doi.day_off_date) = 8 then '해피휴가' else 0 end ) as d8
	,max(case when day(oi.overtime_date) = 9 then oi.overtime_hours when day(doi.day_off_date) = 9 then '해피휴가' else 0 end ) as d9
	,max(case when day(oi.overtime_date) = 10 then oi.overtime_hours when day(doi.day_off_date) = 10 then '해피휴가' else 0 end ) as d10
	,max(case when day(oi.overtime_date) = 11 then oi.overtime_hours when day(doi.day_off_date) = 11 then '해피휴가' else 0 end ) as d11
	,max(case when day(oi.overtime_date) = 12 then oi.overtime_hours when day(doi.day_off_date) = 12 then '해피휴가' else 0 end ) as d12
	,max(case when day(oi.overtime_date) = 13 then oi.overtime_hours when day(doi.day_off_date) = 13 then '해피휴가' else 0 end ) as d13
	,max(case when day(oi.overtime_date) = 14 then oi.overtime_hours when day(doi.day_off_date) = 14 then '해피휴가' else 0 end ) as d14
	,max(case when day(oi.overtime_date) = 15 then oi.overtime_hours when day(doi.day_off_date) = 15 then '해피휴가' else 0 end ) as d15
	,max(case when day(oi.overtime_date) = 16 then oi.overtime_hours when day(doi.day_off_date) = 16 then '해피휴가' else 0 end ) as d16
	,max(case when day(oi.overtime_date) = 17 then oi.overtime_hours when day(doi.day_off_date) = 17 then '해피휴가' else 0 end ) as d17
	,max(case when day(oi.overtime_date) = 18 then oi.overtime_hours when day(doi.day_off_date) = 18 then '해피휴가' else 0 end ) as d18
	,max(case when day(oi.overtime_date) = 19 then oi.overtime_hours when day(doi.day_off_date) = 19 then '해피휴가' else 0 end ) as d19
	,max(case when day(oi.overtime_date) = 20 then oi.overtime_hours when day(doi.day_off_date) = 20 then '해피휴가' else 0 end ) as d20
	,max(case when day(oi.overtime_date) = 21 then oi.overtime_hours when day(doi.day_off_date) = 21 then '해피휴가' else 0 end ) as d21
	,max(case when day(oi.overtime_date) = 22 then oi.overtime_hours when day(doi.day_off_date) = 22 then '해피휴가' else 0 end ) as d22
	,max(case when day(oi.overtime_date) = 23 then oi.overtime_hours when day(doi.day_off_date) = 23 then '해피휴가' else 0 end ) as d23
	,max(case when day(oi.overtime_date) = 24 then oi.overtime_hours when day(doi.day_off_date) = 24 then '해피휴가' else 0 end ) as d24
	,max(case when day(oi.overtime_date) = 25 then oi.overtime_hours when day(doi.day_off_date) = 25 then '해피휴가' else 0 end ) as d25
	,max(case when day(oi.overtime_date) = 26 then oi.overtime_hours when day(doi.day_off_date) = 26 then '해피휴가' else 0 end ) as d26
	,max(case when day(oi.overtime_date) = 27 then oi.overtime_hours when day(doi.day_off_date) = 27 then '해피휴가' else 0 end ) as d27
	,max(case when day(oi.overtime_date) = 28 then oi.overtime_hours when day(doi.day_off_date) = 28 then '해피휴가' else 0 end ) as d28
	,max(case when day(oi.overtime_date) = 29 then oi.overtime_hours when day(doi.day_off_date) = 29 then '해피휴가' else 0 end ) as d29
	,max(case when day(oi.overtime_date) = 30 then oi.overtime_hours when day(doi.day_off_date) = 30 then '해피휴가' else 0 end ) as d30
	,max(case when day(oi.overtime_date) = 31 then oi.overtime_hours when day(doi.day_off_date) = 31 then '해피휴가' else 0 end ) as d31
	,oi.overtime_hours
	,oi.overtime_date
	,u.email
from users u left join overtime_inf oi on u.email = oi.email  left join day_off_inf doi on u.email = doi.email
group by
	email,
	year(oi.overtime_date),
	month(oi.overtime_date) 
) os on u.email = os.email and year(mg.month_start) = os.year and month(mg.month_start) = os.month order by u.email, year(mg.month_start), month(mg.month_start)
