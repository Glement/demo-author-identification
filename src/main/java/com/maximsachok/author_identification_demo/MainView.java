package com.maximsachok.author_identification_demo;

import com.maximsachok.author_identification_demo.Layouts.AuthorIdentificationLayout;
import com.maximsachok.author_identification_demo.Layouts.AuthorLayout;
import com.maximsachok.author_identification_demo.Layouts.ProjectLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.tabs.PagedTabs;


@Route("")
public class MainView extends VerticalLayout {

    private AuthorLayout authorLayout = new AuthorLayout();
    private ProjectLayout projectLayout = new ProjectLayout();
    private AuthorIdentificationLayout authorIdentificationLayout = new AuthorIdentificationLayout();
    private PagedTabs tabs;
    public MainView() {
        VerticalLayout container = new VerticalLayout();
        tabs = new PagedTabs(container);
        tabs.add("Author", authorLayout, false);
        tabs.add("Project", projectLayout, false);
        tabs.add("Find possible Author", authorIdentificationLayout , false);
        add(tabs, container);
    }
}
