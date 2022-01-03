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

consider: https://stackoverflow.com/questions/38857487/why-doesnt-the-qname-for-this-dns-q-end-with-a-null-character#

```
(H)                  (Q)
01 00 5e 00 00 fb 50 46  5d 9f f0 ca 08 00 45 00
00 56 90 f9 40 00 ff 11  47 94 c0 a8 01 65 e0 00
00 fb 14 e9 14 e9 00 42  ee a0 00 00 00 00 00 02
00 00 00 00 00 00 05 57  44 32 54 42 05 6c 6f 63
66 70 6f 76 65 72 74 63  70 04 5f 74 63 70 c0 12
00 21 00 01
```

