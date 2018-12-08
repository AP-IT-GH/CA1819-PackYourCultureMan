using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ASPCoreApi.Models;

namespace ASPCoreApi.Models
{
    public class DatabaseContext : DbContext
    {
        public DatabaseContext(DbContextOptions<DatabaseContext> options) : base(options)
        {

        }       
        public DbSet<Users> users { get; set; }
        public DbSet<Sight> sights { get; set; }
        public DbSet<Streets> streets { get; set; }
        public DbSet<Statistics> stats { get; set; }
        public DbSet<GameStats> gameStats { get; set; }
        public DbSet<VisitedSights> visitedSights { get; set; }

    }
}
