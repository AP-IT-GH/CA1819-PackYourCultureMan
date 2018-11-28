using ASPCoreApi.Controllers;
using ASPCoreApi.Models;
using System;
using System.Collections.Generic;
using Moq;
using Xunit;
using Microsoft.EntityFrameworkCore.Infrastructure;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Mvc;

namespace ASPCore_Testing
{

    public class UnitTest1
    {
        private DatabaseContext databaseContext;
        private List<Sight> _sights = new List<Sight>();

        public UnitTest1()
        {
            var builder = new DbContextOptionsBuilder<DatabaseContext>().UseInMemoryDatabase();
            var context = new DatabaseContext(builder.Options);

            for (int i = 0; i < 5; i++)
            {
                Sight sight = new Sight()
                {
                    id = i,
                    Name = i.ToString(),
                    shortDescription = i.ToString(),
                    longDescription = i.ToString(),
                    Longitude = i.ToString(),
                    Latitude = i.ToString(),
                    sightImage = i.ToString(),
                    Website = i.ToString()

                };
                context.sights.Add(sight);
            }
            databaseContext = context;
            context.SaveChanges();
        }

        [Theory]
        [InlineData(1)]
        public void postTest(int input)
        {
            //Sight sight = new Sight()
            //{
            //    id = input,
            //    Name = input.ToString(),
            //    shortDescription = input.ToString(),
            //    longDescription = input.ToString(),
            //    Longitude = input.ToString(),
            //    Latitude = input.ToString(),
            //    sightImage = input.ToString(),
            //    Website = input.ToString()
            //};
            SightsController sightsController = new SightsController(databaseContext);
            var okResult = sightsController.GetSight(input).Result;
            Assert.IsType<OkObjectResult>(okResult);
        }
    }
}
