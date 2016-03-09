package com.snaphy.mapstrack.Model;

import com.strongloop.android.loopback.Container;
import com.strongloop.android.loopback.File;
import com.strongloop.android.remoting.Transient;


/**
 * Created by Ravi-Gupta on 2/16/2016.
 */
public class CustomFile extends File {

    private String name;
    public void setName(String name) { this.name = name; }

    /**
     * The name of the file, e.g. "image.gif"
     * @return the name
     */
    public String getName() { return name; }

    private String url;
    public void setUrl(String url) { this.url = url; }

    /**
     * The URL of the file.
     * @return the URL
     */
    public String getUrl() {
        return url;
    }

    private Container container;
    @Transient
    public void setContainerRef(Container container) { this.container = container; }
    @Transient
    public Container getContainerRef() { return container; }

    /**
     * Name of the container this file belongs to.
     * @return the container name
     */
    public String getContainer() { return getContainerRef().getName(); }
}
