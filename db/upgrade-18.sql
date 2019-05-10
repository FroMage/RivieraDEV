alter table pricepack add column soldOut bool;
update pricepack set soldOut = false;
