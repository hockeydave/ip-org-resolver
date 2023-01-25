<#macro noauthentication title="Welcome">
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name=viewport content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="/style/reset.css">
        <link rel="stylesheet" href="/style/style.css">
        <link rel="icon" type="image/svg" href="/favicon.svg">
    </head>
    <body>
    <header>
        <div class="container">
            <h1>IP:Org Name Resolver</h1>
        </div>
    </header>

    <main>
        <#nested>
    </main>
    <footer>
        <section class="callout">
            <div class="container">
                an <span class="branded">AppContinuum[]</span> web application with background workers for data collection/analysis.
            </div>
        </section>
        <div class="container">
            <script>document.write("©" + new Date().getFullYear());</script>
            AskusConsulting, Inc. All rights reserved.
        </div>
    </footer>
    </body>
    </html>
</#macro>