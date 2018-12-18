package models;

public enum ConfigurationKey {
    GOOGLE_MAP_API_KEY,
    /* date de début de la conf (format ISO. Ex: 2019-05-15T08:20:00) */
    EVENT_START_DATE,
    /* date de fin de la conf (format ISO. Ex: 2019-05-17T18:00:00) */
    EVENT_END_DATE,
    /*
     * [true, false] Si true, affiche le programme définitif à la place du programme
     * TemporarySlots
     */
    DISPLAY_FULL_SCHEDULE,
    /*
     * [true, false] Si true, affiche les speakers de la nouvelle édition (utile
     * avant que le programme définitif ne soit connu)
     */
    DISPLAY_NEW_SPEAKERS,
    /*
     * [true, false] Si true, affiche dans le menu l'item Talks (utile avant que les
     * talks n'ont pas encore été choisis)
     */
    DISPLAY_TALKS,
    /*
     * [CFP, TICKETS, SPONSORS] Permet de mettre en avant une page spécifique dans
     * la home page et dans le menu
     */
    PROMOTED_PAGE,
    /*
     * [SPONSORS, SCHEDULE] Permet de mettre en avant une 2ème page spécifique dans
     * la home page au dessus du compteur
     */
    PROMOTED_PAGE_2,
    /* Url de la page où on peut acheter les billets */
    TICKETING_URL,
    /* [true, false] Si true, permet d'accéder à la billeterie */
    TICKETING_OPEN
}
