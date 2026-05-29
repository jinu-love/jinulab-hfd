with recursive month_generator as (
select
	cast(:fromDate as date) as month_start
union
    all
select
	month_start + interval 1 month
from
	month_generator
where
	month_start < :toDate )
select
	hpi.project_name as project,
	hpi.project_name as system,
	'(주)아이셋디엑스' as company,
	up.name as name,
	null as sk_pm_leader,
	null as sk_pm_leader_dept,
	hpi.br_name as skhynix_br_name,
	hpi.br_dept as skhynix_br_dept,
	null as contract_yn,
	/*year(mg.month_start) as year,*/
	month(mg.month_start) as month,
	v.total,
        v.d1,
        v.d2,
        v.d3,
        v.d4,
        v.d5,
        v.d6,
        v.d7,
        v.d8,
        v.d9,
        v.d10,
        v.d11,
        v.d12,
        v.d13,
        v.d14,
        v.d15,
        v.d16,
        v.d17,
        v.d18,
        v.d19,
        v.d20,
        v.d21,
        v.d22,
        v.d23,
        v.d24,
        v.d25,
        v.d26,
        v.d27,
        v.d28,
        v.d29,
        v.d30,
        v.d31,
	null as vacation,
	null as note
from
	users u
cross join
        month_generator mg
left outer join user_profiles up on
	u.account_id = up.account_id
left outer join hfd_projectmember_inf hpi on
	u.account_id = hpi.account_id
left outer join
        (
	SELECT
		account_id,
		year,
		month,
		sum(value) as total,
		MAX(case 
                when day = 1 
                    then value 
                else 0 
        end) as d1,
		MAX(case 
            when day = 2 
                then value 
            else 0 
    end) as d2,
		MAX(case 
        when day = 3 
            then value 
        else 0 
end) as d3,
		MAX(case 
    when day = 4 
        then value 
    else 0 
end) as d4,
		MAX(case 
    when day = 5 
        then value 
    else 0 
end) as d5,
		MAX(case 
    when day = 6 
        then value 
    else 0 
end) as d6,
		MAX(case 
    when day = 7 
        then value 
    else 0 
end) as d7,
		MAX(case 
    when day = 8 
        then value 
    else 0 
end) as d8,
		MAX(case 
    when day = 9 
        then value 
    else 0 
end) as d9,
		MAX(case 
    when day = 10 
        then value 
    else 0 
end) as d10,
		MAX(case 
    when day = 11 
        then value 
    else 0 
end) as d11,
		MAX(case 
    when day = 12 
        then value 
    else 0 
end) as d12,
		MAX(case 
    when day = 13 
        then value 
    else 0 
end) as d13,
		MAX(case 
    when day = 14 
        then value 
    else 0 
end) as d14,
		MAX(case 
    when day = 15 
        then value 
    else 0 
end) as d15,
		MAX(case 
    when day = 16 
        then value 
    else 0 
end) as d16,
		MAX(case 
    when day = 17 
        then value 
    else 0 
end) as d17,
		MAX(case 
    when day = 18 
        then value 
    else 0 
end) as d18,
		MAX(case 
    when day = 19 
        then value 
    else 0 
end) as d19,
		MAX(case 
    when day = 20 
        then value 
    else 0 
end) as d20,
		MAX(case 
    when day = 21 
        then value 
    else 0 
end) as d21,
		MAX(case 
    when day = 22 
        then value 
    else 0 
end) as d22,
		MAX(case 
    when day = 23 
        then value 
    else 0 
end) as d23,
		MAX(case 
    when day = 24 
        then value 
    else 0 
end) as d24,
		MAX(case 
    when day = 25 
        then value 
    else 0 
end) as d25,
		MAX(case 
    when day = 26 
        then value 
    else 0 
end) as d26,
		MAX(case 
    when day = 27 
        then value 
    else 0 
end) as d27,
		MAX(case 
    when day = 28 
        then value 
    else 0 
end) as d28,
		MAX(case 
    when day = 29 
        then value 
    else 0 
end) as d29,
		MAX(case 
    when day = 30 
        then value 
    else 0 
end) as d30,
		MAX(case 
    when day = 31 
        then value 
    else 0 
end) as d31
	FROM
		(
		select
			account_id,
			dayoff_date as date,
			year(dayoff_date) as year,
			month(dayoff_date) as month,
			day(dayoff_date) as day,
			ifnull(dayoff_type, '해피휴가') as value
		from
			hfd_dayoff_inf
		group by
			account_id,
			dayoff_date,
			year(dayoff_date),
			month(dayoff_date),
			day(dayoff_date)
	union
    all
		SELECT
			account_id,
			overtime_date as date,
			year(overtime_date) as year,
			month(overtime_date) as month,
			day(overtime_date) as day,
			overtime_hours as value
		FROM
			hfd_overtime_inf
		group by
			account_id,
			overtime_date,
			year(overtime_date),
			month(overtime_date),
			day(overtime_date)) as t
	group by
		account_id,
		year,
		month
	order by
		account_id,
		year,
		month ) v 
        on
	u.account_id = v.account_id
	and year(mg.month_start) = v.year
	and month(mg.month_start) = v.month
WHERE
	up.department = :dept
order by
	hpi.project_name,
	up.name,
	year(mg.month_start),
	month(mg.month_start)