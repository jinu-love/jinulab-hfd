select
	u.name,
	u.dept,
	oi.overtime_date,
	oi.overtime_hours,
	oi.comment,
	oi.updated_date
from
	overtime_inf oi
inner join users u on
	oi.email = u.email
	and u.name = :name
order by
	oi.overtime_date desc