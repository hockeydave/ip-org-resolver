<#import "template.ftl" as layout />

<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                Hello, You sent IP address: ${ip}!
            </p>
        </div>
    </section>

</@layout.noauthentication>