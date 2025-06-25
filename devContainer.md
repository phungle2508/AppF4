jhipster import-jdl ../docs/jdl/app.jdl --force
git submodule update --init --recursive

sudo apt install -y wget apt-transport-https curl gnupg
wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | sudo tee /etc/apt/trusted.gpg.d/adoptium.asc
echo "deb https://packages.adoptium.net/artifactory/deb $(lsb_release -sc) main" | sudo tee /etc/apt/sources.list.d/adoptium.list
sudo apt update
sudo apt install -y temurin-21-jdk

sudo apt install -y maven


curl -fsSL https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.5/install.sh | bash
source ~/.bashrc
nvm install --lts
nvm use --lts

curl -fsSL https://get.pnpm.io/install.sh | sh
export PATH="$HOME/.local/share/pnpm:$PATH"
pnpm -v -->

pnpm config set registry https://registry.npmjs.org/

cd frontend

corepack enable
corepack prepare pnpm@latest --activate

pnpm install

pnpm turbo run build  # Builds all packages
pnpm turbo run dev    # Runs in development mode    