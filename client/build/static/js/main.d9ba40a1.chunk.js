(this.webpackJsonpclient=this.webpackJsonpclient||[]).push([[0],{16:function(e,t,c){},18:function(e,t,c){},26:function(e,t,c){},27:function(e,t,c){},28:function(e,t,c){"use strict";c.r(t);var n=c(10),r=c.n(n),i=(c(16),c(5)),s=c(1);var a=function(){return Object(s.jsxs)("div",{className:"App",children:[Object(s.jsx)(i.b,{to:"/writeCircolare",children:"scrivi la circolare"}),Object(s.jsx)("br",{}),Object(s.jsx)(i.b,{to:"/circolari",children:"visualizza le circolari"})]})},o=c(11),l=c(4),j=c(0),u=(c(18),c(19),function(){var e=Object(j.useState)(["tutti"]),t=Object(l.a)(e,2),c=t[0],n=t[1];Object(j.useEffect)((function(){return e=n,void fetch("http://localhost:5000/filters",{method:"POST",mode:"cors",headers:{"Content-Type":"application/json"}}).then((function(e){return e.json()})).then((function(t){return e(["tutti"].concat(Object(o.a)(t)))})).catch((function(e){return console.log(e)}));var e}),[]);return Object(s.jsxs)("form",{onSubmit:function(e){var t={},c=[];e.preventDefault();for(var n=e.currentTarget.elements,r=0;r<n.length-1;r++){var i=n.item(r).name,s=n.item(r).value;"on"===s&&(s=n.item(r).checked),"boolean"!==typeof s?t[i]=s:!0===s&&c.push(i)}t.tags=c,fetch("http://localhost:5000/circolari/write",{method:"POST",mode:"cors",headers:{"Content-Type":"application/json"},body:JSON.stringify(t)}).then((function(e){200===e.status?alert("Circolare pubblicata"):alert("wrong password")})).catch((function(e){return console.log(e)}))},className:"formWrite",id:"form",children:[Object(s.jsx)("input",{name:"id",type:"number",placeholder:"id:",className:"inputID",required:!0}),Object(s.jsx)("br",{}),Object(s.jsx)("input",{name:"titolo",type:"text",placeholder:"titolo:",className:"inputTitolo",required:!0}),Object(s.jsx)("br",{}),Object(s.jsx)("textarea",{name:"descrizione",placeholder:"descrizione:",className:"inputDescrizione",required:!0}),Object(s.jsx)("br",{}),Object(s.jsx)("input",{name:"password",type:"password",placeholder:"password:",className:"inputPassword",required:!0}),Object(s.jsx)("br",{}),null===c||void 0===c?void 0:c.map((function(e,t){return Object(s.jsxs)("label",{className:"switch",children:[e,Object(s.jsx)("input",{name:e,type:"checkbox",className:"filter"}),Object(s.jsx)("span",{className:"slider"})]},t)})),Object(s.jsx)("br",{}),Object(s.jsx)("input",{type:"submit",value:"Submit",className:"submitButton"})]})}),b=(c(26),function(e){var t=e.circolare;return Object(s.jsx)(s.Fragment,{children:Object(s.jsx)("div",{className:"container",children:Object(s.jsxs)(i.b,{to:"/circolari/".concat(t.id),className:"link",children:[Object(s.jsx)("p",{className:"number",children:t.id}),Object(s.jsx)("p",{className:"title",children:t.title})]})})})}),d=function(){var e=Object(j.useState)([]),t=Object(l.a)(e,2),c=t[0],n=t[1];return Object(j.useEffect)((function(){fetch("http://localhost:5000/circolari").then((function(e){return e.json()})).then((function(e){n(e.sort((function(e,t){return t.id-e.id})))})).catch((function(e){return console.log(e)}))}),[]),Object(s.jsx)(s.Fragment,{children:c.map((function(e){return Object(s.jsx)(b,{circolare:e},e.id)}))})},h=c(2),p=(c(27),function(){var e=Object(h.g)(),t=Object(j.useState)({id:-1,title:"empty",description:"error, can't find on DB",tags:[]}),c=Object(l.a)(t,2),n=c[0],r=c[1];return Object(j.useEffect)((function(){fetch("http://localhost:5000/circolari/".concat(e.id||"")).then((function(e){return e.json()})).then((function(e){r(e)})).catch((function(e){return console.log(e)}))}),[]),Object(s.jsx)(s.Fragment,{children:Object(s.jsxs)("div",{className:"circolare",children:[Object(s.jsxs)("p",{className:"id",children:["N: ",n.id]}),Object(s.jsxs)("p",{className:"title",children:["title: ",n.title]}),Object(s.jsx)("p",{className:"description",children:n.description}),Object(s.jsx)("p",{className:"tags",children:JSON.stringify(n.tags)})]})})});r.a.render(Object(s.jsx)(i.a,{children:Object(s.jsxs)(h.c,{children:[Object(s.jsx)(h.a,{path:"/",element:Object(s.jsx)(a,{})}),Object(s.jsx)(h.a,{path:"/writeCircolare",element:Object(s.jsx)(u,{})}),Object(s.jsx)(h.a,{path:"circolari",element:Object(s.jsx)(d,{})}),Object(s.jsx)(h.a,{path:"circolari/:id",element:Object(s.jsx)(p,{})})]})}),document.getElementById("root"))}},[[28,1,2]]]);
//# sourceMappingURL=main.d9ba40a1.chunk.js.map