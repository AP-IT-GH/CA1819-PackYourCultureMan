using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

namespace ASPCoreApi.Migrations
{
    public partial class floattodouble : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<double>(
                name: "Longitude",
                table: "sights",
                nullable: false,
                oldClrType: typeof(float));

            migrationBuilder.AlterColumn<double>(
                name: "Latitude",
                table: "sights",
                nullable: false,
                oldClrType: typeof(float));

            migrationBuilder.CreateTable(
                name: "statistics",
                columns: table => new
                {
                    id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    highestScore = table.Column<int>(nullable: false),
                    totalScore = table.Column<int>(nullable: false),
                    totalFailed = table.Column<int>(nullable: false),
                    totalSucces = table.Column<int>(nullable: false),
                    totalLost = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_statistics", x => x.id);
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "statistics");

            migrationBuilder.AlterColumn<float>(
                name: "Longitude",
                table: "sights",
                nullable: false,
                oldClrType: typeof(double));

            migrationBuilder.AlterColumn<float>(
                name: "Latitude",
                table: "sights",
                nullable: false,
                oldClrType: typeof(double));
        }
    }
}
