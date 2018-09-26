-- Drinks(drink_id, drink_name, price, image, description)
CREATE TABLE Drinks (
  drink_id integer primary key autoincrement,
  drink_name varchar(40) not null,
  price float not null,
  image blob,
  description varchar(120)
);

-- Ingredients(ingredient_id, ingredient_name)
CREATE TABLE Ingredients (
  ingredient_id integer primary key autoincrement,
  ingredient_name varchar(40) not null
);

-- DrinkIngredients(drink_id, ingredient_id, amount)
CREATE TABLE DrinkIngredients (
  drink_id integer not null,
  ingredient_id integer not null,
  amount integer,
  PRIMARY KEY (drink_id, ingredient_id),
  FOREIGN KEY (drink_id) REFERENCES Drinks(drink_id),
  FOREIGN KEY (ingredient_id) REFERENCES Ingredients(ingredient_id)
);

-- FreqMade(user_id, drink_id, times_made)
-- TODO: how is this different than UsersOrderDrinks
CREATE TABLE FreqMade (
  user_id integer not null,
  drink_id integer not null,
  times_made integer,
  PRIMARY KEY (user_id, drink_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
  FOREIGN KEY (drink_id) REFERENCES Drinks(drink_id)
);

-- HasSpecial(user_id, drink_id, special_price, special_type)
CREATE TABLE HasSpecial (
  user_id integer not null,
  drink_id integer not null,
  special_price float,
  special_type varchar(40),
  PRIMARY KEY (user_id, drink_id),
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
  FOREIGN KEY (drink_id) REFERENCES Drinks(drink_id)
);

-- User(user_id, login_name, login_pass, image)
CREATE TABLE Users (
  user_id integer primary key autoincrement,
  user_login varchar(40) not null,
  user_pass varchar(40) not null,
  image blob
);

-- Bartender(bartender_id, pin_number, permissions)
Create TABLE Bartender (
  bartender_id integer primary key autoincrement,
  bartender_pin varchar(4),
  bartender_permission varchar(10)
);

-- Regular(regualr_id, usual_drink)
CREATE TABLE Regular (
  regualr_id integer primary key autoincrement,
  usual_drink varchar(40)
)

-- UsersOrderDrinks(user_id, drink_id, number)
CREATE TABLE UsersOrderDrinks (
  user_id integer not null,
  drink_id integer not null,
  numbers integer,
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
  FOREIGN KEY (drink_id) REFERENCES Drinks(drink_id)
);

-- Tabs(user_id, tab_id, open_closed, time_open)
CREATE TABLE Tabs (
  tab_id integer primary key autoincrement,
  user_id integer not null,
  open_closed boolean,
  time_open text,       -- https://www.sqlite.org/lang_datefunc.html
  FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

-- TabsHaveDrink(user_id, tab_id, drink_id, number)
CREATE TABLE TabsHaveDrink (
  user_id integer not null,
  tab_id integer not null,
  drink_id integer not null,
  num integer,
);

-- Slots(slot_number, bottle_name, starting_volume, current_volume, time_loaded)
CREATE TABLE Slots (
  slot_number integer primary key autoincrement,
  bottle_name varchar(40) not null,
  starting_volume integer,
  current_volume integer,
  time_loaded text          -- https://www.sqlite.org/lang_datefunc.html
);
