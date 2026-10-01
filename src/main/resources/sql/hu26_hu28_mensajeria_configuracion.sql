IF OBJECT_ID('dbo.configuracion', 'U') IS NULL
BEGIN

    CREATE TABLE dbo.configuracion (
        idConfiguracion BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        idioma NVARCHAR(50) NULL,
        tamanioFuente NVARCHAR(50) NULL,
        temaVisual NVARCHAR(50) NULL,
        notificacionesSonido BIT NULL,
        idUsuario BIGINT NOT NULL,

        CONSTRAINT FK_configuracion_usuario
            FOREIGN KEY (idUsuario)
            REFERENCES dbo.usuario(idUsuario)
    );

END;
GO
