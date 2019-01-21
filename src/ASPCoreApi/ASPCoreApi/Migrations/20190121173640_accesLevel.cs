using Microsoft.EntityFrameworkCore.Migrations;

namespace ASPCoreApi.Migrations
{
    public partial class accesLevel : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "accessLevel",
                table: "users");

            migrationBuilder.AddColumn<bool>(
                name: "accesLevel",
                table: "users",
                nullable: false,
                defaultValue: false);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "accesLevel",
                table: "users");

            migrationBuilder.AddColumn<int>(
                name: "accessLevel",
                table: "users",
                nullable: false,
                defaultValue: 0);
        }
    }
}
