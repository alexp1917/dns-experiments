# names

> some dns experiments

Initially this was intended to be a fully standalone dns solution,
but then it turns out parsing and generating dns packets is quite
complex and it would probably be a better solution to try to reuse
some existing libraries for this. The `names.model` package hasn't
been fully reimagined in this new context yet. It's unlikely that
it needs to be very low level. Maybe it needs to be revisited after
all its consuming packages are implemented.

Instead, the direction now is to try to get basic server and CRUD
functionality implemented.
