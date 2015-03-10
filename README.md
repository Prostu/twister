# Twister

A Twitter clone for funzies. I aim to learn and practice new things with it.

The first things to try are:
* PostgreSQL
* Play2 Framework
* React

The first iteration will use PostgreSQL for persistent storage, Play2 for a JSON API and React as the app.

# Documentation:
## Objects
### `User`
```
{
  id: Number,
  handle: String,
  name: String,
  joined: Number
}
```
joined is given in unix time.

### `Tweet`
```
{
  id: Number,
  user_id: Number,
  message: String
}
```