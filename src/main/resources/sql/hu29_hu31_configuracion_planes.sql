IF OBJECT_ID('dbo.plan', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.plan (
        id_plan BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nombre_plan NVARCHAR(255) NOT NULL,
        precio FLOAT NOT NULL,
        limite_recuerdos INT NOT NULL,
        descripcion NVARCHAR(MAX) NULL
    );

END;
GO
