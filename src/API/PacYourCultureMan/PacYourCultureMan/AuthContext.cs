using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PacYourCultureMan
{
        public class AuthContext : IdentityDbContext<Microsoft.AspNet.Identity.EntityFramework.IdentityUser>
        {
            public AuthContext()
                : base("AuthContext")
            {

            }
        }
}
