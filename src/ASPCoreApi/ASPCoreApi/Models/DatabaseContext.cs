﻿using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ASPCoreApi.Models
{
    public class DatabaseContext : DbContext
    {
        public DatabaseContext(DbContextOptions<DatabaseContext> options) : base(options)
        {

        }

        public DbSet<Users> users { get; set; }
        public DbSet<Sight> sights { get; set; }
        public DbSet<Dot> dots { get; set; }
        public DbSet<Statistics> statistics { get; set; }

    }
}
