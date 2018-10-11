using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.Owin;
using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;
[assembly: OwinStartup(typeof(PacYourCultureMan.TokenStartup))]
namespace PacYourCultureMan
{
        public class TokenStartup
        {
            public void Configuration(IAppBuilder app)
            {
                HttpConfiguration config = new HttpConfiguration();
                WebApiConfig.Register(config);
                app.UseWebApi(config);
            }

        }
}
