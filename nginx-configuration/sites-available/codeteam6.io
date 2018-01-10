limit_req_zone $binary_remote_addr zone=one:10m rate=1r/m;
limit_req_zone $binary_remote_addr zone=two:10m rate=1r/m;

server {
  server_tokens 	off;
##### GEOIP SETTINGS #####
  if ($allowed_country = no) {
    return 444;
  }
##########################
  listen 80;
  server_name	codeteam6.io www.codeteam6.io;
  root		/var/www/codeteam6.io;
  return        301 https://$server_name$request_uri;
}

server {
  server_tokens 	off;
##### GEOIP SETTINGS #####
  if ($allowed_country = no) {
    return 444;
  }

#### SSL SETTINGS ####
  listen        443 ssl http2;
  ssl_certificate         /etc/nginx/codeteam6_fullchain.pem;
  ssl_certificate_key     /etc/nginx/codeteam6_privkey.pem;
  ssl_trusted_certificate /etc/nginx/codeteam6_chain.pem;
  ssl_session_timeout     1d;
  ssl_session_cache       shared:SSL:50m;
  ssl_dhparam             /etc/nginx/dhparams.pem;
  ssl_protocols           TLSv1 TLSv1.1 TLSv1.2;
  ssl_ciphers 'EECDH+AESGCM:EDH+AESGCM:AES256+EECDH:AES256+EDH';
  ssl_prefer_server_ciphers       on;
  add_header              Strict-Transport-Security "max-age=2592000";
  add_header              X-Content-Type-Options "nosniff" always;
  add_header              X-Frame-Options "SAMEORIGIN" always;
  add_header              X-Xss-Protection "1; mode=block";
  add_header              Content-Security-Policy "default-src 'none'; script-src 'self' https://maps.google.com https://maps.googleapis.com; frame-src; connect-src 'self'; img-src 'self' https://maps.gstatic.com https://csi.gstatic.com https://maps.google.com https://maps.googleapis.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com; manifest-src https://codeteam6.io; form-action https://codeteam6.io/api/; report-uri https://rtl.report-uri.com/r/default/csp/enforce;"; 
  add_header              Referrer-Policy "strict-origin-when-cross-origin";
  add_header              Expect-CT "max-age=0, report-uri https://rtl.report-uri.com/r/d/ct/reportOnly";
  ssl_stapling            on;
  ssl_stapling_verify     on;
  resolver                8.8.8.8 8.8.4.4 valid=600s;
  resolver_timeout        60s;
###############################
# add_header link "</styles/vendor.dfe1e24f.css>; rel=preload; as=style";
# add_header link "</styles/vendor.dfe1e24f.css>; rel=preload; as=style";
# add_header link "</scripts/vendor.fe93b402.js>; rel=preload; as=script";
# add_header link "</scripts/scripts.87ad6667.js>; rel=preload; as=script";
#############################
  root /var/www/codeteam6.io;
  index index.html;

  brotli_static on;
  gzip_static	on;



  server_name codeteam6.io www.codeteam6.io;
  location =/index.html {
#    add_header link "</styles/vendor.dfe1e24f.css>; rel=preload; as=style";
#    add_header link "</styles/vendor.dfe1e24f.css>; rel=preload; as=style";
#    add_header link "</scripts/vendor.fe93b402.js>; rel=preload; as=script";
#    add_header link "</scripts/scripts.87ad6667.js>; rel=preload; as=script";
    add_header              Strict-Transport-Security "max-age=2592000";
    add_header              X-Content-Type-Options "nosniff" always;
    add_header              X-Frame-Options "SAMEORIGIN" always;
    add_header              X-Xss-Protection "1; mode=block";
    add_header              Content-Security-Policy "default-src 'none'; script-src 'self' https://maps.google.com https://maps.googleapis.com; frame-src; connect-src 'self'; img-src 'self' https://maps.gstatic.com https://csi.gstatic.com https://maps.google.com https://maps.googleapis.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com; manifest-src https://codeteam6.io; form-action https://codeteam6.io/api/; report-uri https://rtl.report-uri.com/r/default/csp/enforce;"; 
 
    add_header              Referrer-Policy "strict-origin-when-cross-origin";
    add_header              Expect-CT "max-age=0, report-uri https://rtl.report-uri.com/r/d/ct/reportOnly";
  }

  location ~* \.(css|js|png|gif|jpg|ico)$ {
    expires 1M;
    add_header              Strict-Transport-Security "max-age=2592000";
    add_header              X-Content-Type-Options "nosniff" always;
    add_header              X-Frame-Options "SAMEORIGIN" always;
    add_header              X-Xss-Protection "1; mode=block";
    add_header              Content-Security-Policy "default-src 'none'; script-src 'self' https://maps.google.com https://maps.googleapis.com; frame-src; connect-src 'self'; img-src 'self' https://maps.gstatic.com https://csi.gstatic.com https://maps.google.com https://maps.googleapis.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com; manifest-src https://codeteam6.io; form-action https://codeteam6.io/api/; report-uri https://rtl.report-uri.com/r/default/csp/enforce;"; 
  }

  location ~*\.jar {
   return 403;
  }

  location / {
    try_files $uri $uri/ =404;
  }

  location /api/users/authrider {
    limit_req zone=one burst=1 nodelay;
    limit_req_status 429;
    proxy_set_header X-Forwarded-Host $host;
    proxy_set_header X-Forwarded-Server $host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_pass http://127.0.0.1:8081/api/users/authrider;
  }
  location /api/users/auth {
    limit_req zone=two burst=1 nodelay;
    limit_req_status 429;
    proxy_set_header X-Forwarded-Host $host;
    proxy_set_header X-Forwarded-Server $host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_pass http://127.0.0.1:8081/api/users/auth;
  }
  location /api/ {
    proxy_set_header X-Forwarded-Host $host;
    proxy_set_header X-Forwarded-Server $host;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_pass http://127.0.0.1:8081/api/;
  }
}
