select
	u.name,
	u.dept,
	di.day_off_date,
	di.updated_date,
	di.comment
from
	day_off_inf di
inner join users u on
	di.email = u.email
	and u.name = :name
order by
	di.day_off_date desc