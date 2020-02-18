public class MyData extends Data implements Cloneable{
    /* OVERVIEW
     *      Data è un tipo di dato astratto e immutabile che estende Data
     *      Un dato è caratterizzato da un titolo e da un contenuto
     *
     * AF(c) = < {c.title, c.content} >
     * I(c) = c.title != null && c.content != null
     */

    private String title;
    private String content;

    // Costruttore: richiede un titolo non null
    public MyData(String title) throws NullPointerException {
        if(title == null) throw new NullPointerException();
        this.title = title;
        this.content = "---";
    }

    // Costruttore: richiede un titolo non null e un contenuto
    public MyData(String title, String content) throws NullPointerException {
        if(title == null) throw new NullPointerException();
        this.title = title;

        if(content == null) {
            this.content = "---";
        } else {
            this.content = content;
        }
    }

    // Stampa a video il titolo e il contenuto del dato
    public void display() {
        System.out.println(title);
        System.out.println(content);
    }

    // Restituisce il titolo del dato
    public String getTitle() { return title; }

    // Restiruisce il contenuto del dato
    public String getContent() { return content; }
}

