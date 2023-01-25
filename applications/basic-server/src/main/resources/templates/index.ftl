<#import "template.ftl" as layout />

<@layout.noauthentication>
    <section>
        <div class="container">
            <p>
                Enter the IP Address that you want to resolve to an Organization name
            <form action="/ip-resolve" method="post">
                <input name="ip" placeholder="IP-Address e.g. 192.168.1.1">
                <br>

                <br>
                <button>Submit</button>
            </form>
            </p>
        </div>

    </section>

</@layout.noauthentication>