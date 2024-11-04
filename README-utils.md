-- create a configMap from .env file
```
kubectl create configmap my-config --from-env-file=.env --dry-run=client -o yaml > my-config.yaml
```