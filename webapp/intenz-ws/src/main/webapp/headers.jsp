<%@ taglib prefix="b" uri="http://www.ebi.ac.uk/biobabel" %>
<header>
    <b:include url="${initParam.templatesUrl}/masthead/global/services"/>
    <b:include url="${initParam.templatesUrl}/masthead/local" method="POST">
        <b:param name="">
{
	"localMasthead": {
		"title": "IntEnz",
        "homepageUrl": "..",
		"navigationTabs": [{
			"selected": ${param.loc eq 'home'},
			"title": "Home",
			"id": "intenz_tab_home",
			"tooltip": "Home page",
			"href": "../index.jsp"
		},{
			"selected": ${param.loc eq 'browse'},
			"title": "Browse",
			"id": "intenz_tab_browse",
			"tooltip": "Browse the EC classification",
			"href": "../browse.jsp"
		},{
			"selected": ${param.loc eq 'submissions'},
			"title": "Submit",
			"id": "intenz_tab_submit",
			"tooltip": "Submit new enzyme data",
			"href": "../submissions.jsp"
		},{
			"selected": ${param.loc eq 'download'},
			"title": "Download",
			"id": "intenz_tab_download",
			"tooltip": "Download IntEnz data if the format of your choice",
			"href": "../downloads.jsp"
		},{
			"selected": ${param.loc eq 'docs'},
			"title": "Documentation",
			"id": "intenz_tab_docs",
			"tooltip": "Documentation about IntEnz and the EC classification and nomenclature",
			"href": "../faq.jsp"
		},{
			"selected": ${param.loc eq 'about'},
			"title": "About",
			"id": "intenz_tab_about",
			"tooltip": "About IntEnz",
			"href": "../about.jsp"
		}],
        "functionalTabs": [{
            "title": "Feedback",
            "href": "../contact.jsp",
            "id": "intenz_tab_feedback",
            "tooltip": "Contact the IntEnz team",
            "iconFontSet": "icon-static",
            "dataIcon": "\\"
        }],
        "search": {
            "action": "../query",
            "method": "get",
            "inputName": "q",
            "advanced": "../search.jsp",
            "examples": [{
                "title": "1.1.1.1",
                "href": "../query?cmd=Search&q=1.1.1.1"
            },{
                "title": "alcohol%",
                "href": "../query?cmd=Search&q=alcohol%"
            },{
                "title": "alpha-glucosidase",
                "href": "../query?cmd=Search&q=alpha-glucosidase"
            }]
        }
    }
}
        </b:param>
    </b:include>
</header>
