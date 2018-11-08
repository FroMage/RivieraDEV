package models;

public enum ConfigurationKey {
	GOOGLE_MAP_API_KEY,
	DISPLAY_FULL_SCHEDULE, // [true, false] Si true, affiche le programme définitif à la place du programme TemporarySlots
	DISPLAY_NEW_SPEAKERS,  // [true, false] Si true, affiche les speakers de la nouvelle édition (utile avant que le programme définitif ne soit connu)
	DISPLAY_TALKS, 		   // [true, false] Si true, affiche dans le menu l'item Talks (utile avant que les talks n'ont pas encore été choisis)
	PROMOTED_PAGE,         // [CFP, TICKETS, SPONSORS] Permet de mettre en avant une page spécifique dans la home page et dans le menu
	PROMOTED_PAGE_2,       // [SPONSORS, SCHEDULE] Permet de mettre en avant une 2ème page spécifique dans la home page au dessus du compteur
	TICKETING_URL,         // Url de la page où on peut acheter les billets
	TICKETING_OPEN         // [true, false] Si true, permet d'accéder à la billeterie
}
